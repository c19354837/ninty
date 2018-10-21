package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiString;

/**
 * Created by ninty on 2018/10/21
 */
public class NaClassLoader {
    private final static String className = "java/lang/ClassLoader";

    public static void init() {
        NaMethodManager.register(className, "findLoadedClass0", "(Ljava/lang/String;)Ljava/lang/Class;", new
                findLoadedClass0());
    }

    public static class findLoadedClass0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            String classname = NiString.getString(frame.getLocalVars().getRef(1));
            NiClass clz = frame.getMethod().getClz().getLoader().getClass(classname);
            frame.getOperandStack().pushRef(clz != null ? clz.getjClass() : null);
        }
    }
}
