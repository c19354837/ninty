package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;

/**
 * Created by ninty on 2017/8/19.
 */
public class NaDouble {
    private final static String className = "java/lang/Double";

    public static void init() {
        NaMethodManager.register(className, "doubleToRawLongBits", "(D)J", new doubleToRawLongBits());
    }

    public static class doubleToRawLongBits implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            double value = localVars.getDouble(0);
            long result = Double.doubleToRawLongBits(value);
            frame.getOperandStack().pushLong(result);
        }
    }
}
