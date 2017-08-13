package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiClassLoader;
import com.ninty.runtime.heap.NiObject;
import com.ninty.runtime.heap.NiString;

/**
 * Created by ninty on 2017/8/13.
 */
public class NaObject {

    public static void init() {
        NaMethodManager.register("java/lang/Object", "getClass", "()Ljava/lang/Class", new getClass());
        NaMethodManager.register("java/lang/Object", "getPrimitiveClass", "()Ljava/lang/Class", new getPrimitiveClass());
        NaMethodManager.register("java/lang/Object", "getName0", "()Ljava/lang/Class", new getName0());
        NaMethodManager.register("java/lang/Object", "desiredAssertionStatus0", "()Ljava/lang/Class", new desiredAssertionStatus0());
    }

    public static class getClass implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            NiObject jClass = self.getClz().getjClass();
            frame.getOperandStack().pushRef(jClass);
        }
    }

    public static class getPrimitiveClass implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject ref = frame.getLocalVars().getRef(0);
            String name = NiString.getString(ref);
            NiClassLoader loader = frame.getMethod().getClz().getLoader();
            NiObject jClass = loader.loadClass(name).getjClass();
            frame.getOperandStack().pushRef(jClass);
        }
    }

    public static class getName0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            NiClass clz = (NiClass) self.getExtra();
            String name = clz.javaName();
            NiObject nameObj = NiString.newString(clz.getLoader(), name);
            frame.getOperandStack().pushRef(nameObj);
        }
    }

    public static class desiredAssertionStatus0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushInt(0);
        }
    }
}
