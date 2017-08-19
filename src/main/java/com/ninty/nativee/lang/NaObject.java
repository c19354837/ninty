package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiObject;

/**
 * Created by ninty on 2017/8/13.
 */
public class NaObject {

    private final static String className = "java/lang/Object";

    public static void init() {
        NaMethodManager.register(className, "getClass", "()Ljava/lang/Class;", new getClass());
    }

    public static class getClass implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            NiObject jClass = self.getClz().getjClass();
            frame.getOperandStack().pushRef(jClass);
        }
    }
}
