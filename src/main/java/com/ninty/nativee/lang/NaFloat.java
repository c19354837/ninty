package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;

/**
 * Created by ninty on 2017/8/19.
 */
public class NaFloat {
    private final static String className = "java/lang/Float";

    public static void init() {
        NaMethodManager.register(className, "floatToRawIntBits", "(F)I", new floatToRawIntBits());
    }

    public static class floatToRawIntBits implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            float value = localVars.getFloat(0);
            int result = Float.floatToRawIntBits(value);
            frame.getOperandStack().pushInt(result);
        }
    }
}
