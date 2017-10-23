package com.ninty.runtime;

import com.ninty.cmd.base.CmdFatory;
import com.ninty.cmd.base.ICmdBase;
import com.ninty.runtime.heap.*;
import com.sun.jdi.NativeMethodException;

/**
 * Created by ninty on 2017/7/12.
 */
public class NiThread {
    private int level;
    private NiStack stack;
    private NiObject currentThread; // java.lang.Thread

    private static NiObject mainThread;

    private static final String CLZ_THREAD = "java/lang/Thread";

    public NiThread(int maxStackSize) {
        stack = new NiStack(maxStackSize);
    }

    public void generateThread(NiObject threadGroup, NiClassLoader loader, String name) {
        NiClass clz = loader.loadClass(CLZ_THREAD);
        NiObject thread = clz.newObject();
        thread.setFieldInt("priority", Thread.NORM_PRIORITY);
        NiMethod constructor = clz.getInitMethod("(Ljava/lang/ThreadGroup;Ljava/lang/String;)V");
        currentThread = thread;
        execMethod(constructor, new Slot(thread), new Slot(threadGroup), new Slot(NiString.newString(loader, name)));
    }

    public NiFrame popFrame() {
        level--;
        NiFrame frame = stack.pop();
        NiFrame top = topFrame();
        if (top != null) {
            top.restorePostion();
        }
        return frame;
    }

    public void pushFrame(NiFrame frame) {
        level++;
        NiFrame top = topFrame();
        if (top != null) {
            top.savePosition();
        }
        frame.reset();
        frame.setThread(this);
        stack.push(frame);
    }

    public static Slot execMethodDirectly(NiMethod method, Slot... params) {
        NiThread thread = new NiThread(64);
        if (mainThread == null) {
            throw new NullPointerException("mainThread is null");
        }
        thread.setCurrentThread(mainThread);
        NiFrame returnFrame = NiFrame.RETURN_FRAME;
        thread.pushFrame(returnFrame);

        thread.execMethod(method, params);
        if (returnFrame.getOperandStack().getSize() > 0) {
            return returnFrame.getOperandStack().popSlot();
        }
        return new Slot();
    }

    public void execMethod(NiMethod method, Slot... params) {
        fillParas(method, params);
        execThread();
    }

    public void invokeMethod(NiMethod method) {
        int argsCount = method.getArgsCount();
        Slot[] params = new Slot[argsCount];
        OperandStack stack = topFrame().getOperandStack();
        if (argsCount > 0) {
            for (int i = argsCount - 1; i >= 0; i--) {
                params[i] = stack.popSlot();
            }
        }
        fillParas(method, params);
    }

    private void fillParas(NiMethod method, Slot... params) {
        NiFrame newFrame = new NiFrame(method);
        pushFrame(newFrame);
        int argsCount = method.getArgsCount();
        LocalVars slots = newFrame.getLocalVars();
        if (argsCount > 0) {
            for (int i = argsCount - 1; i >= 0; i--) {
                slots.setSlot(i, params[i]);
            }
        }
    }

    private void execThread() {
        try {
            long startTime = System.nanoTime();
            System.out.println("start\n");
            while (true) {
                NiFrame frame = topFrame();
                if (frame == NiFrame.RETURN_FRAME) {
                    break;
                }
                CodeBytes bb = frame.getCode();
                byte opCode = frame.getOpCode();
                ICmdBase cmd = CmdFatory.getCmd(opCode);
                try {
//                    System.out.println(getT(getLevel()) + cmd.getClass().getSimpleName());
//                    System.out.println(getT(getLevel()) + frame);
//                    System.out.println();

                    cmd.init(bb);
                    cmd.exec(frame);
                } catch (Exception e) {
                    throwException(frame, e);
                }

                if (isEmpty()) {
                    break;
                }
            }
            System.out.println("\nspend " + (System.nanoTime() - startTime) / Math.pow(10, 6) + "ms");
            System.out.println("\n**done**");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void throwException(NiFrame frame, Exception e) throws Exception {
        Class eClz = e.getClass();
        if (eClz == NativeMethodException.class) {
            throw e;
        }
        frame.restorePostion();
        // new
        NiClass exClz = frame.getMethod().getClz().getLoader().loadClass(eClz.getName());

        // dup
        if (frame.getOperandStack().getSize() < 2) {
            frame.setOperandStack(new OperandStack(2));
        }
        OperandStack stack = frame.getOperandStack();
        NiObject exObj = exClz.newObject();
        stack.clear();
        stack.pushRef(exObj);
        stack.pushRef(exObj);

        // init
        NiMethod initMethod = exClz.getMethod("<init>", "()V");
        frame.getThread().invokeMethod(initMethod);

        NiFrame topFrame = frame.getThread().topFrame();
        while (topFrame != frame) {
            CodeBytes bb = topFrame.getCode();
            byte opCode = topFrame.getOpCode();
            ICmdBase cmd = CmdFatory.getCmd(opCode);
            cmd.init(bb);
            cmd.exec(topFrame);
            topFrame = frame.getThread().topFrame();
        }

        // athrow
        ICmdBase athrow = CmdFatory.getCmd((byte) 0xbf);
        athrow.exec(frame);
    }

    private String getT(int level) {
        StringBuilder t = new StringBuilder(level);
        for (int i = 1; i < level; i++) {
            t.append('\t');
        }
        return t.toString();
    }

    public NiObject getCurrentThread() {
        return currentThread;
    }

    public static void setMainThread(NiObject mainThread) {
        NiThread.mainThread = mainThread;
    }

    public void setCurrentThread(NiObject currentThread) {
        this.currentThread = currentThread;
    }

    public NiFrame topFrame() {
        return stack.top();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int getLevel() {
        return level;
    }
}
