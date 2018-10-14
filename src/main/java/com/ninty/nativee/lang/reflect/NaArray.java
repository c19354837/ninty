package com.ninty.nativee.lang.reflect;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiObject;

/**
 * Created by ninty on 2018/10/13
 */
public class NaArray {
    private final static String className = "java/lang/reflect/Array";

    public static void init() {
        NaMethodManager.register(className, "newArray", "(Ljava/lang/Class;I)Ljava/lang/Object;", new newArray());
    }

    public static class newArray implements INativeMethod {

        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject clzObj = localVars.getRef(0);
            int length = localVars.getInt(1);
            if (length < 0) {
                throw new NegativeArraySizeException(length + "");
            }

            NiClass clz = ((NiClass)clzObj.getExtra()).getArrayClass();
            NiObject result = clz.newArray(length);
            frame.getOperandStack().pushRef(result);
        }
    }
}
