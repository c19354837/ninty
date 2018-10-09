package com.ninty.nativee.lang.invoke;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;

public class NaMethodHandleNatives {
    private final static String className = "java/lang/invoke/MethodHandleNatives";

    public static void init() {
        NaMethodManager.register(className, "getConstant", "(I)I", new
                getConstant());
    }

    public static class getConstant implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            int which = frame.getLocalVars().getInt(0);
            frame.getOperandStack().pushInt(which == 4 ? 1 : 0);
        }
    }
}
