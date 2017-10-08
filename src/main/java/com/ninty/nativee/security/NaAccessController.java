package com.ninty.nativee.security;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiObject;

/**
 * Created by ninty on 2017/10/6.
 */
public class NaAccessController {
    private final static String className = "java/security/AccessController";

    public static void init() {
        NaMethodManager.register(className, "getStackAccessControlContext", "()Ljava/security/AccessControlContext;",
                new getStackAccessControlContext());
        NaMethodManager.register(className, "doPrivileged", "(Ljava/security/PrivilegedAction;)Ljava/lang/Object;",
                new doPrivileged());
    }

    public static class getStackAccessControlContext implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushRef(null);
        }
    }

    public static class doPrivileged implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject ref = frame.getLocalVars().getRef(0);
            frame.getOperandStack().pushRef(ref);
            frame.getThread().invokeMethod(ref.getClz().getMethod("run", "()Ljava/lang/Object;"));
        }
    }
}
