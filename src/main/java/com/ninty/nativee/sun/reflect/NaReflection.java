package com.ninty.nativee.sun.reflect;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiObject;

/**
 * Created by ninty on 2017/10/8.
 */
public class NaReflection {
    private final static String className = "sun/reflect/Reflection";

    public static void init() {
        NaMethodManager.register(className, "getClassAccessFlags", "(Ljava/lang/Class;)I",
                new getClassAccessFlags());
    }

    public static class getClassAccessFlags implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            frame.getOperandStack().pushInt(((NiClass)self.getExtra()).getAccessFlags());
        }
    }
}
