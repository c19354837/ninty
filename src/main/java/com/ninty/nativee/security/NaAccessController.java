package com.ninty.nativee.security;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;

/**
 * Created by ninty on 2017/10/6.
 */
public class NaAccessController {
    private final static String className = "java/security/AccessController";

    public static void init() {
        NaMethodManager.register(className, "getStackAccessControlContext", "()Ljava/security/AccessControlContext;",
                new NaAccessController.getStackAccessControlContext());
    }

    public static class getStackAccessControlContext implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushRef(null);
        }
    }
}
