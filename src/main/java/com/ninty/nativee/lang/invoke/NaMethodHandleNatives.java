package com.ninty.nativee.lang.invoke;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiObject;
import com.ninty.runtime.heap.NiString;

public class NaMethodHandleNatives {
    private final static String className = "java/lang/invoke/MethodHandleNatives";

    public static void init() {
        NaMethodManager.register(className, "getConstant", "(I)I", new
                getConstant());
        NaMethodManager.register(className, "resolve", "(Ljava/lang/invoke/MemberName;Ljava/lang/Class;)Ljava/lang/invoke/MemberName;", new
                resolve());
    }

    public static class getConstant implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            int which = frame.getLocalVars().getInt(0);
            frame.getOperandStack().pushInt(which == 4 ? 1 : 0);
        }
    }

    public static class resolve implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            NiObject memberName = frame.getLocalVars().getRef(0);
            NiObject caller = frame.getLocalVars().getRef(1);
            if (caller == null) {
                caller = memberName.getFieldRef("clazz", "Ljava/lang/Class;");
            }
            // TODO: getMethod with name and desc
//            NiObject type = memberName.getFieldRef("type", "Ljava/lang/Object;");
//            NiObject rtypes = type.getFieldRef("rtype", "Ljava/lang/Class;");
//            NiObject ptypes = type.getFieldRef("ptypes", "[Ljava/lang/Class;");
            NiClass callerClz = caller.getClzByExtra();
            int flags = memberName.getFieldInt("flags");
            String name = NiString.getString(memberName.getFieldRef("name", "Ljava/lang/String;"));
            memberName.setFieldInt("flags", flags | callerClz.getMethodByName(name).getAccessFlags());
            frame.getOperandStack().pushRef(memberName);
        }
    }
}
