package com.ninty.startup;

import com.ninty.classpath.ClassPath;
import com.ninty.cmd.base.CmdFatory;
import com.ninty.cmd.base.ICmdBase;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiStack;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiClassLoader;
import com.ninty.runtime.heap.NiMethod;

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

    private void resolveClass() {
        NiClassLoader loader = new NiClassLoader(cp);
        NiClass clz = loader.loadClass(className);
        NiMethod mainMethod = clz.getMainMethod();
        if (mainMethod == null) {
            throw new RuntimeException("Can't find main method in [" + className + "]");
        }

        NiThread thread = new NiThread(64);
        NiFrame frame = new NiFrame(thread, mainMethod);
        NiStack stack = thread.getStack();
        stack.push(frame);

        execThread(thread);
    }

    private void execThread(NiThread thread) {
        NiFrame frame = thread.getStack().pop();
        ByteBuffer bb = frame.getCode();
        try {
            while (true) {
                byte opCode = bb.get();
                ICmdBase cmd = CmdFatory.getCmd(opCode);
                cmd.init(bb);
                cmd.exec(frame);

                System.out.println(cmd.getClass().getSimpleName());
                System.out.println(frame);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
