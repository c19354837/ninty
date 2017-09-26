package com.ninty.startup;

import com.ninty.classpath.ClassPath;
import com.ninty.cmd.CmdReferences;
import com.ninty.cmd.base.CmdFatory;
import com.ninty.cmd.base.ICmdBase;
import com.ninty.nativee.NintyJavaCache;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.OperandStack;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiClassLoader;
import com.ninty.runtime.heap.NiMethod;
import com.ninty.runtime.heap.NiObject;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/5.
 */
public class BootStartup {

    /**
     * classpath
     */
    private String className;

    private ClassPath cp;

    public BootStartup(String userCP, String className) {
        this.className = className;
        cp = new ClassPath(null, userCP);
        resolveClass();
    }

    //TODO -XX
    private void resolveClass() {
        NiClassLoader loader = new NiClassLoader(cp);
        NintyJavaCache.setClassLoader(loader);
        NiClass clz = loader.loadClass(className);
        NiMethod mainMethod = clz.getMainMethod();
        if (mainMethod == null) {
            throw new RuntimeException("Can't find main method in [" + className + "]");
        }

        NiThread thread = new NiThread(64);
        NiFrame frame = new NiFrame(mainMethod);
        thread.pushFrame(frame);

        execThread(thread);
    }

    private void execThread(NiThread thread) {
        try {
            long startTime = System.nanoTime();
            System.out.println("start\n");
            while (true) {
                NiFrame frame = thread.topFrame();
                ByteBuffer bb = frame.getCode();
                byte opCode = frame.getOpCode();
                ICmdBase cmd = CmdFatory.getCmd(opCode);
                try {
                    cmd.init(bb);
                    cmd.exec(frame);
                } catch (Exception e) {
                    throwException(frame, e);
                }

//                System.out.println(getT(thread.getLevel()) + cmd.getClass().getSimpleName());
//                System.out.println(getT(thread.getLevel()) + frame);
//                System.out.println();

                if (thread.isEmpty()) {
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

}
