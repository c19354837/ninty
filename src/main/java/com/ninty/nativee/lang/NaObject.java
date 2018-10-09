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
        NaMethodManager.register(className, "hashCode", "()I", new hashCode());
        NaMethodManager.register(className, "notify", "()V", new notify());
        NaMethodManager.register(className, "notifyAll", "()V", new notifyAll());
        NaMethodManager.register(className, "wait", "()V", new wait());
        NaMethodManager.register(className, "wait", "(J)V", new wait_j());
        NaMethodManager.register(className, "clone", "()Ljava/lang/Object;", new clone());
    }

    public static class getClass implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            NiObject jClass = self.getClz().getjClass();
            frame.getOperandStack().pushRef(jClass);
        }
    }

    public static class hashCode implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            int hashCode = self.hashCode();
            frame.getOperandStack().pushInt(hashCode);
        }
    }

    public static class notify implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            System.out.println(Thread.currentThread() + ": notify");
            NiObject self = frame.getLocalVars().getThis();
            synchronized (self) {
                self.notify();
            }
        }
    }

    public static class notifyAll implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            synchronized (self) {
                self.notifyAll();
            }
        }
    }

    public static class wait implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            NaObject.wait(self, 0);
        }
    }

    public static class wait_j implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            long waitTime = frame.getLocalVars().getInt(1);
            NaObject.wait(self, waitTime);
        }
    }

    private static void wait(NiObject self, long waitTime) {
        System.out.println(Thread.currentThread() + ": wait");
        synchronized (self) {
            self.unlock();
            try {
                self.wait(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        self.lock();
    }


    public static class clone implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            try {
                frame.getOperandStack().pushRef(self.clone());
            } catch (CloneNotSupportedException e) {
                throw new UnsupportedOperationException("CloneNotSupportedException");
            }
        }
    }
}
