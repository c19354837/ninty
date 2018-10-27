package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiObject;
import com.ninty.runtime.heap.NiString;

/**
 * Created by ninty on 2018/10/24
 */
public class NaClassLoader$NativeLibrary {
    private final static String classname = "java/lang/ClassLoader$NativeLibrary";

    public static void init() {
        NaMethodManager.register(classname, "findBuiltinLib", "(Ljava/lang/String;)Ljava/lang/String;", new
                findBuiltinLib());
        NaMethodManager.register(classname, "load", "(Ljava/lang/String;Z)V", new
                load());
    }

    public static class findBuiltinLib implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject ref = frame.getLocalVars().getRef(0);
            String libname = NiString.getString(ref);
            frame.getOperandStack().pushRef(ref);
        }
    }

    public static class load implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
        }
    }

}
