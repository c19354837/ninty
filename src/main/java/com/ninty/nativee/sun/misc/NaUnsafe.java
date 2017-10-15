package com.ninty.nativee.sun.misc;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by ninty on 2017/10/8.
 */
public class NaUnsafe {
    private final static String className = "sun/misc/Unsafe";

    public static void init() {
        NaMethodManager.register(className, "arrayBaseOffset", "(Ljava/lang/Class;)I",
                new arrayBaseOffset());
        NaMethodManager.register(className, "arrayIndexScale", "(Ljava/lang/Class;)I",
                new arrayIndexScale());
        NaMethodManager.register(className, "addressSize", "()I",
                new addressSize());
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
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                Unsafe theUnsafe = (Unsafe) field.get(null);
                frame.getOperandStack().pushInt(theUnsafe.addressSize());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
