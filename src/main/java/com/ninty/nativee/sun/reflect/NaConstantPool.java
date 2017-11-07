package com.ninty.nativee.sun.reflect;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiObject;
import com.ninty.runtime.heap.NiString;
import com.ninty.runtime.heap.constantpool.NiConstant;
import com.ninty.runtime.heap.constantpool.NiConstantPool;

public class NaConstantPool {
    private final static String className = "sun/reflect/ConstantPool";

    public static void init() {
        NaMethodManager.register(className, "getUTF8At0", "(Ljava/lang/Object;I)Ljava/lang/String;",
                new getUTF8At0());
    }

    public static class getUTF8At0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject self = localVars.getThis();
//            NiObject cps = localVars.getRef(1);
            int index = localVars.getInt(2);
            NiConstantPool cps = (NiConstantPool) self.getExtra();
            NiConstant.NiUtf8 utf8 = (NiConstant.NiUtf8) cps.get(index);
            NiObject ref = NiString.newString(frame.getMethod().getClz().getLoader(), utf8.value);
            frame.getOperandStack().pushRef(ref);
        }
    }
}
