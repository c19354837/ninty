package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiClassLoader;
import com.ninty.runtime.heap.NiObject;
import com.ninty.runtime.heap.NiString;
import com.ninty.utils.VMUtils;

/**
 * Created by ninty on 2017/8/13.
 */
public class NaClass {

    private final static String className = "java/lang/Class";

    public static void init() {
        NaMethodManager.register(className, "getPrimitiveClass", "(Ljava/lang/String;)Ljava/lang/Class;", new
                getPrimitiveClass());
        NaMethodManager.register(className, "getName0", "()Ljava/lang/String;", new getName0());
        NaMethodManager.register(className, "desiredAssertionStatus0", "(Ljava/lang/Class;)Z", new
                desiredAssertionStatus0());
        NaMethodManager.register(className, "getDeclaringClass0", "()Ljava/lang/Class;", new
                getDeclaringClass0());
        NaMethodManager.register(className, "isArray", "()Z", new isArray());
        NaMethodManager.register(className, "getEnclosingMethod0", "()[Ljava/lang/Object;", new getEnclosingMethod0());
        NaMethodManager.register(className, "getModifiers", "()I", new getModifiers());
        NaMethodManager.register(className, "isPrimitive", "()Z", new isPrimitive());
    }

    public static class getPrimitiveClass implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject ref = frame.getLocalVars().getThis();
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
            frame.getOperandStack().pushBoolean(false);
        }
    }

    public static class getDeclaringClass0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushRef(null);
        }
    }

    public static class isArray implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            frame.getOperandStack().pushBoolean(self.getClz().isArray());
        }
    }

    public static class getEnclosingMethod0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushRef(null);
        }
    }

    public static class getModifiers implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushInt(frame.getMethod().getClz().getAccessFlags());
        }
    }

    public static class isPrimitive implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            boolean isPrimitive = VMUtils.primitiveTypes.containsKey(self.getClz().getClassName());
            frame.getOperandStack().pushBoolean(isPrimitive);
        }
    }
}
