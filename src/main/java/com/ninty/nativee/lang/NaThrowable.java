package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiMethod;
import com.ninty.runtime.heap.NiObject;

/**
 * Created by ninty on 2017/8/13.
 */
public class NaThrowable {

    private final static String className = "java/lang/Throwable";

    public static void init() {
        NaMethodManager.register(className, "fillInStackTrace", "(I)Ljava/lang/Throwable;", new fillInStackTrace());
    }

    public static class fillInStackTrace implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            frame.getOperandStack().pushRef(self);

            int skip = distanceToObject(self.getClz()) + 2;
            int total = frame.getThread().getLevel();
            NiFrame prevFrame = frame;
            StackTraceElement[] stes = new StackTraceElement[total];
            for (int i = total - 1; i <= 0; i--) {
                if (i > total - skip) {
                    return;
                }
                stes[i] = new StackTraceElement(prevFrame);
                prevFrame = prevFrame.prevFrame;
            }
            self.setExtra(stes);
        }

        private int distanceToObject(NiClass clz) {
            int distance = 0;
            for (NiClass sclz = clz.getSuperClass(); sclz != null; sclz = sclz.getSuperClass()) {
                distance++;
            }
            return distance;
        }

        private static class StackTraceElement {
            String fileName;
            String className;
            String methodName;
            int lineNumber;

            StackTraceElement(NiFrame frame) {
                NiMethod method = frame.getMethod();
                NiClass clz = method.getClz();
                fileName = clz.getSourceFile();
                className = clz.getClassName();
                methodName = method.getName();
                lineNumber = method.getLineNumber(frame.getPosition());
            }
        }
    }

}
