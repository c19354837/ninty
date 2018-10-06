package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
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
        NaMethodManager.register(className, "getRawAnnotations", "()[B", new getRawAnnotations());
        NaMethodManager.register(className, "getConstantPool", "()Lsun/reflect/ConstantPool;", new getConstantPool());
        NaMethodManager.register(className, "forName0", "(Ljava/lang/String;ZLjava/lang/ClassLoader;Ljava/lang/Class;)Ljava/lang/Class;", new forName0());
        NaMethodManager.register(className, "isInterface", "()Z", new isInterface());
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

    public static class getRawAnnotations implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            NiClass clz = (NiClass) self.getExtra();
            byte[] annotationDatas = clz.getAnnotationDatas();
            if (annotationDatas == null) {
                frame.getOperandStack().pushRef(null);
            } else {
                NiClass arrClz = frame.getMethod().getClz().getLoader().loadClass("[B");
                NiObject obj = new NiObject(arrClz, annotationDatas);
                frame.getOperandStack().pushRef(obj);
            }
        }
    }

    public static class getConstantPool implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            NiClass clz = (NiClass) self.getExtra();
            NiObject obj = clz.getLoader().loadClass("sun/reflect/ConstantPool").newObject();
            obj.setExtra(clz.getCps());
            frame.getOperandStack().pushRef(obj);
        }
    }

    public static class forName0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject classname = localVars.getRef(0);
            boolean inited = localVars.getBoolean(1);
//            NiObject classLoader = localVars.getRef(2);
//            NiObject caller = localVars.getRef(3);

            String name = NiString.getString(classname);
            name = name.replace('.', '/');
            NiClass clz = frame.getMethod().getClz().getLoader().loadClass(name);
            if (inited) {
                frame.restorePostion();
                clz.clinit(frame.getThread());
            } else {
                frame.getOperandStack().pushRef(clz.getjClass());
            }
        }
    }
}
