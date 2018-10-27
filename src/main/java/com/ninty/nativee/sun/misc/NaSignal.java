package com.ninty.nativee.sun.misc;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiString;

/**
 * Created by ninty on 2018/10/27
 */
public class NaSignal {
    private final static String className = "sun/misc/Signal";

    public static void init() {
        NaMethodManager.register(className, "findSignal", "(Ljava/lang/String;)I",
                new findSignal());
        NaMethodManager.register(className, "handle0", "(IJ)J",
                new handle0());
    }

    public static class findSignal implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            String name = NiString.getString(frame.getLocalVars().getRef(0));
            frame.getOperandStack().pushInt(0);
        }
    }

    public static class handle0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushLong(0);
        }
    }
}
