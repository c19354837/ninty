package com.ninty.nativee.sun.misc;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
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
            if (obj.getFields().getRef(offset) == expect) {
                obj.getFields().setRef(offset, update);
                frame.getOperandStack().pushBoolean(true);
            } else {
                frame.getOperandStack().pushBoolean(false);
            }
        }
    }
}
