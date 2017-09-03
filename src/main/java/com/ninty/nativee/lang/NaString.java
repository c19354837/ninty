package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiObject;
import com.ninty.runtime.heap.NiString;

/**
 * Created by ninty on 2017/9/3.
 */
public class NaString {
    private final static String className = "java/lang/String";

    public static void init() {
        NaMethodManager.register(className, "intern", "()Ljava/lang/String;", new NaString.intern());
    }

    public static class intern implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject ref = frame.getLocalVars().getThis();
            NiString.intern(ref);
            frame.getOperandStack().pushRef(ref);
        }
    }
}
