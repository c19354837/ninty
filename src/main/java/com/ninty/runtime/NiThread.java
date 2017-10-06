package com.ninty.runtime;

import com.ninty.cmd.CmdReferences;
import com.ninty.cmd.base.CmdFatory;
import com.ninty.cmd.base.ICmdBase;
import com.ninty.runtime.heap.*;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/12.
 */
public class NiThread {
    private int level;
    private NiStack stack;
    private NiObject currentThread; // java.lang.Thread

    private static final String CLZ_THREAD = "java/lang/Thread";

    public NiThread(int maxStackSize, NiClassLoader loader) {
        stack = new NiStack(maxStackSize);
    }

    public void generateThread(NiObject threadGroup, NiClassLoader loader, String name) {
        NiClass clz = loader.loadClass(CLZ_THREAD);
        NiObject thread = clz.newObject();
        thread.setField("priority", "I", Thread.NORM_PRIORITY);
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

    public void execMethod(NiMethod method, Slot... params) {
        NiFrame newFrame = new NiFrame(method);
        pushFrame(newFrame);
        int argsCount = method.getArgsCount();
        LocalVars slots = newFrame.getLocalVars();
        if (params.length > 0) {
            for (int i = argsCount - 1; i >= 0; i--) {
                slots.setSlot(i, params[i]);
            }
        }
        System.out.println("invoke method: " + method);
        execThread();
    }

    public void invokeMethod(NiMethod method) {
        NiFrame frame = topFrame();
        NiFrame newFrame = new NiFrame(method);
        pushFrame(newFrame);
        int argsCount = method.getArgsCount();
        OperandStack stack = frame.getOperandStack();
        LocalVars slots = newFrame.getLocalVars();
        if (argsCount > 0) {
            for (int i = argsCount - 1; i >= 0; i--) {
                slots.setSlot(i, stack.popSlot());
            }
        }
        System.out.println("invoke method: " + method);
    }

    public void execThread() {
        try {
            long startTime = System.nanoTime();
            System.out.println("start\n");
            while (true) {
                NiFrame frame = topFrame();
                ByteBuffer bb = frame.getCode();
                byte opCode = frame.getOpCode();
                ICmdBase cmd = CmdFatory.getCmd(opCode);
                //                try {
                cmd.init(bb);
                cmd.exec(frame);
                //                } catch (Exception e) {
                //                    throwException(frame, e);
                //                }

                //                System.out.println(getT(getLevel()) + cmd.getClass().getSimpleName());
                //                System.out.println(getT(getLevel()) + frame);
                //                System.out.println();

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

    private void throwException(NiFrame frame, Exception e) {
        frame.restorePostion();
        // new
        NiClass exClz = frame.getMethod().getClz().getLoader().loadClass(e.getClass().getName());

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
        CmdReferences.invokeMethod(frame, initMethod);

        NiFrame topFrame = frame.getThread().topFrame();
        while (topFrame != frame) {
            ByteBuffer bb = topFrame.getCode();
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
