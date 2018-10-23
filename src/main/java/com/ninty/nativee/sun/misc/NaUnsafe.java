package com.ninty.nativee.sun.misc;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiObject;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by ninty on 2017/10/8.
 */
public class NaUnsafe {
    private static Unsafe unsafe;

    static {
        Field field;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private final static String className = "sun/misc/Unsafe";

    public static void init() {
        NaMethodManager.register(className, "arrayBaseOffset", "(Ljava/lang/Class;)I",
                new arrayBaseOffset());
        NaMethodManager.register(className, "arrayIndexScale", "(Ljava/lang/Class;)I",
                new arrayIndexScale());
        NaMethodManager.register(className, "addressSize", "()I",
                new addressSize());
        NaMethodManager.register(className, "allocateMemory", "(J)J",
                new allocateMemory());
        NaMethodManager.register(className, "putLong", "(JJ)V",
                new putLong());
        NaMethodManager.register(className, "getByte", "(J)B",
                new getByte());
        NaMethodManager.register(className, "freeMemory", "(J)V",
                new freeMemory());
        NaMethodManager.register(className, "objectFieldOffset", "(Ljava/lang/reflect/Field;)J",
                new objectFieldOffset());
        NaMethodManager.register(className, "compareAndSwapObject", "(Ljava/lang/Object;JLjava/lang/Object;Ljava/lang/Object;)Z",
                new compareAndSwapObject());
        NaMethodManager.register(className, "compareAndSwapInt", "(Ljava/lang/Object;JII)Z",
                new compareAndSwapInt());
        NaMethodManager.register(className, "getIntVolatile", "(Ljava/lang/Object;J)I",
                new getIntVolatile());
        NaMethodManager.register(className, "getObjectVolatile", "(Ljava/lang/Object;J)Ljava/lang/Object;",
                new getObjectVolatile());
        NaMethodManager.register(className, "putObjectVolatile", "(Ljava/lang/Object;JLjava/lang/Object;)V",
                new putObjectVolatile());
        NaMethodManager.register(className, "compareAndSwapLong", "(Ljava/lang/Object;JJJ)Z",
                new compareAndSwapLong());
        NaMethodManager.register(className, "shouldBeInitialized", "(Ljava/lang/Class;)Z",
                new shouldBeInitialized());
        NaMethodManager.register(className, "ensureClassInitialized", "(Ljava/lang/Class;)V",
                new ensureClassInitialized());
        NaMethodManager.register(className, "staticFieldOffset", "(Ljava/lang/reflect/Field;)J",
                new staticFieldOffset());
        NaMethodManager.register(className, "staticFieldBase", "(Ljava/lang/reflect/Field;)Ljava/lang/Object;",
                new staticFieldBase());
        NaMethodManager.register(className, "defineAnonymousClass", "(Ljava/lang/Class;[B[Ljava/lang/Object;)Ljava/lang/Class;",
                new defineAnonymousClass());
    }

    public static class arrayBaseOffset implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushInt(0);
        }
    }

    public static class arrayIndexScale implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushInt(1);
        }
    }

    public static class addressSize implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushInt(unsafe.addressSize());
        }
    }

    public static class allocateMemory implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            long size = frame.getLocalVars().getLong(1);
            long addr = unsafe.allocateMemory(size);
            frame.getOperandStack().pushLong(addr);
        }
    }

    public static class putLong implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            long l1 = localVars.getLong(1);
            long l2 = localVars.getLong(3);
            unsafe.putLong(l1, l2);
        }
    }

    public static class getByte implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            long addr = localVars.getLong(1);
            byte data = unsafe.getByte(addr);
            frame.getOperandStack().pushInt(data);
        }
    }

    public static class freeMemory implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            long addr = localVars.getLong(1);
            unsafe.freeMemory(addr);
        }
    }

    public static class objectFieldOffset implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject fieldObj = localVars.getRef(1);
            frame.getOperandStack().pushLong(fieldObj.getFieldInt("slot"));
        }
    }

    // TODO: CAS
    public static class compareAndSwapObject implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject obj = localVars.getRef(1);
            int offset = (int) localVars.getLong(2);
            NiObject expect = localVars.getRef(4);
            NiObject update = localVars.getRef(5);
            if (obj.getClz().isArray()) {
                if (obj.aobject()[offset] == expect) {
                    obj.aobject()[offset] = update;
                    frame.getOperandStack().pushBoolean(true);
                } else {
                    frame.getOperandStack().pushBoolean(false);
                }
            } else {
                if (obj.getFields().getRef(offset) == expect) {
                    obj.getFields().setRef(offset, update);
                    frame.getOperandStack().pushBoolean(true);
                } else {
                    frame.getOperandStack().pushBoolean(false);
                }
            }

        }
    }

    public static class compareAndSwapInt implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject obj = localVars.getRef(1);
            int offset = (int) localVars.getLong(2);
            int expect = localVars.getInt(4);
            int update = localVars.getInt(5);
            if (obj.getClz().isArray()) {
                if (obj.aint()[offset] == expect) {
                    obj.aint()[offset] = update;
                    frame.getOperandStack().pushBoolean(true);
                } else {
                    frame.getOperandStack().pushBoolean(false);
                }
            } else {
                if (obj.getFields().getInt(offset) == expect) {
                    obj.getFields().setInt(offset, update);
                    frame.getOperandStack().pushBoolean(true);
                } else {
                    frame.getOperandStack().pushBoolean(false);
                }
            }
        }
    }

    public static class compareAndSwapLong implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject obj = localVars.getRef(1);
            int offset = (int) localVars.getLong(2);
            long expect = localVars.getInt(4);
            long update = localVars.getInt(6);
            if (obj.getClz().isArray()) {
                if (obj.along()[offset] == expect) {
                    obj.along()[offset] = update;
                    frame.getOperandStack().pushBoolean(true);
                } else {
                    frame.getOperandStack().pushBoolean(false);
                }
            } else {
                if (obj.getFields().getLong(offset) == expect) {
                    obj.getFields().setLong(offset, update);
                    frame.getOperandStack().pushBoolean(true);
                } else {
                    frame.getOperandStack().pushBoolean(false);
                }
            }
        }
    }

    public static class getIntVolatile implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject obj = localVars.getRef(1);
            long offset = localVars.getLong(2);
            if (obj.getClz().isArray()) {
                frame.getOperandStack().pushInt(obj.aint()[(int) offset]);
            } else {
                LocalVars fields = obj.getFields();
                frame.getOperandStack().pushInt(fields.getInt((int) offset));
            }
        }
    }

    public static class getObjectVolatile implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject obj = localVars.getRef(1);
            long offset = localVars.getLong(2);
            if (obj.getClz().isArray()) {
                frame.getOperandStack().pushRef(obj.aobject()[(int) offset]);
            } else {
                LocalVars fields = obj.getFields();
                frame.getOperandStack().pushRef(fields.getRef((int) offset));
            }
        }
    }

    public static class putObjectVolatile implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject obj = localVars.getRef(1);
            long offset = localVars.getLong(2);
            NiObject val = localVars.getRef(4);
            if (obj.getClz().isArray()) {
                obj.aobject()[(int) offset] = val;
            } else {
                LocalVars fields = obj.getFields();
                fields.setRef((int) offset, val);
            }
        }
    }

    public static class shouldBeInitialized implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushBoolean(false);
        }
    }

    public static class ensureClassInitialized implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject cObj = frame.getLocalVars().getRef(1);
            NiClass clz = cObj.getClzByExtra();
            if (!clz.isClinit()) {
                frame.restorePostion();
                clz.clinit(frame.getThread());
            }
        }
    }

    public static class staticFieldOffset implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject fieldObj = frame.getLocalVars().getRef(1);
            frame.getOperandStack().pushLong(fieldObj.getFieldInt("slot"));
        }
    }

    public static class staticFieldBase implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject fieldObj = frame.getLocalVars().getRef(1);
            frame.getOperandStack().pushRef(fieldObj.getClz().getjClass());
        }
    }

    public static class defineAnonymousClass implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject hoster = localVars.getRef(1);
            NiObject datas = localVars.getRef(2);
            NiClass clz = hoster.getClz().getLoader().loadClass(datas.abyte());
            frame.getOperandStack().pushRef(clz.getjClass());
        }
    }
}
