package com.ninty.startup;

import com.ninty.classfile.AttributeInfo;
import com.ninty.classfile.ClassFile;
import com.ninty.classfile.MemberInfo;
import com.ninty.classpath.ClassPath;
import com.ninty.cmd.base.CmdFatory;
import com.ninty.cmd.base.ICmdBase;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiStack;
import com.ninty.runtime.NiThread;

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
        ClassFile file = new ClassFile(cp.readClass(className));
        MemberInfo mainMethod = getMainMethod(file);
        if (mainMethod == null) {
            throw new RuntimeException("Can't find main method in [" + className + "]");
        }

        AttributeInfo.AttrCode attrCode = mainMethod.getAttrCode();
        NiThread thread = new NiThread(64);
        NiFrame frame = new NiFrame(attrCode.maxLocals, attrCode.maxStack, attrCode.codes);
        NiStack stack = thread.getStack();
        stack.push(frame);

        execThread(thread);
    }

    private MemberInfo getMainMethod(ClassFile file) {
        MemberInfo[] methodInfos = file.getMethodInfos();
        for (MemberInfo method : methodInfos) {
            if (method.getName().equals("main") && method.getDesc().equals("([Ljava/lang/String;)V")) {
                return method;
            }
        }
        return null;
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
