package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;

public class NaRuntime {

    private final static String className = "java/lang/Runtime";

    public static void init() {
        NaMethodManager.register(className, "availableProcessors", "()I", new availableProcessors());
    }

    public static class availableProcessors implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushInt(Runtime.getRuntime().availableProcessors());
        }
    }

}
