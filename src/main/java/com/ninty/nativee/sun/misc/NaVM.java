package com.ninty.nativee.sun.misc;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.Slot;
import com.ninty.runtime.heap.*;

/**
 * Created by ninty on 2017/10/8.
 */
public class NaVM {
    private final static String className = "sun/misc/VM";

    public static void init() {
        NaMethodManager.register(className, "initialize", "()V",
                new initialize());
    }

    public static class initialize implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiClass clzVM = frame.getMethod().getClz();
            NiClassLoader loader = clzVM.getLoader();
            NiObject objSavedProps = clzVM.getStaticRef("savedProps", "Ljava/util/Properties;");
            NiMethod metSetProperty = objSavedProps.getClz().getMethod("setProperty", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
            NiThread.execMethodDirectly(metSetProperty,
                    new Slot(objSavedProps),
                    new Slot(NiString.newString(loader, "java.lang.Integer.IntegerCache.high")),
                    new Slot(NiString.newString(loader, "127")));

            // TODO
        }
    }
}
