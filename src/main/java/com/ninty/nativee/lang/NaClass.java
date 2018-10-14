package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.Slot;
import com.ninty.runtime.heap.*;

import java.util.Arrays;

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
        NaMethodManager.register(className, "getDeclaredFields0", "(Z)[Ljava/lang/reflect/Field;", new getDeclaredFields0());
        NaMethodManager.register(className, "isInterface", "()Z", new isInterface());
        NaMethodManager.register(className, "getComponentType", "()Ljava/lang/Class;", new getComponentType());
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
            NiObject self = frame.getLocalVars().getThis();
            NiClass clz = self.getClzByExtra();
            if (clz.isArray() || clz.isPrimitive()) {
                frame.getOperandStack().pushRef(null);
                return;
            }
            int innerClz = clz.getClassName().lastIndexOf('$');
            if (innerClz < 0) {
                frame.getOperandStack().pushRef(null);
                return;
            }

            String declaringClzName = clz.getClassName().substring(0, innerClz);
            NiClass declaringClz = frame.getMethod().getClz().getLoader().loadClass(declaringClzName);
            frame.getOperandStack().pushRef(declaringClz.getjClass());
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
            boolean isPrimitive = self.getClzByExtra().isPrimitive();
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

    public static class getDeclaredFields0 implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject self = localVars.getThis();
            boolean publicOnly = localVars.getBoolean(1);
            NiField[] fields = Arrays.stream(self.getClzByExtra().getFields()).filter((field) -> !publicOnly || field.isPublic()).toArray(NiField[]::new);

            NiClass clz = (NiClass) self.getExtra();
            NiClassLoader loader = clz.getLoader();
            NiClass fieldsClz = loader.loadClass("[Ljava/lang/reflect/Field;");
            int count = fields.length;
            NiObject result = fieldsClz.newArray(count);
            frame.getOperandStack().pushRef(result);

            if (count == 0) {
                return;
            }

            NiClass fieldClz = loader.loadClass("java/lang/reflect/Field");
            for (int i = 0; i < count; i++) {
                NiField field = fields[i];

                NiObject fieldObj = fieldClz.newObject();
                NiMethod initMethod = fieldObj.getClz().getInitMethod("(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;IILjava/lang/String;[B)V");
                NiThread.execMethodDirectly(initMethod,
                        new Slot(fieldObj),
                        new Slot(self),
                        new Slot(NiString.newString(loader, field.getName())),
                        new Slot(field.getType().getjClass()),
                        new Slot(field.getAccessFlags()),
                        new Slot(field.getSlotId()),
                        new Slot(NiString.newString(loader, field.getSignature())),
                        new Slot(loader.loadClass("[B").newArray(0)) // TODO: annotation bytes
                );
                result.aobject()[i] = fieldObj;
            }
        }
    }

    public static class isInterface implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            boolean result = self.getClz().isInterface();
            frame.getOperandStack().pushBoolean(result);
        }
    }

    public static class getComponentType implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            frame.getOperandStack().pushRef(self.getClzByExtra().componentClass().getjClass());
        }
    }
}

