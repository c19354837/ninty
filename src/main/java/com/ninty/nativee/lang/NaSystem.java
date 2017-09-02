package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiObject;

/**
 * Created by ninty on 2017/8/19.
 */
public class NaSystem {

    private final static String className = "java/lang/System";

    public static void init() {
        NaMethodManager.register(className, "arraycopy", "(Ljava/lang/Object;ILjava/lang/Object;II)V", new arraycopy());
    }

    public static class arraycopy implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject src = localVars.getRef(0);
            int srcPos = localVars.getInt(1);
            NiObject desc = localVars.getRef(2);
            int descPos = localVars.getInt(3);
            int length = localVars.getInt(4);
            if (!src.getClz().isArray() || !desc.getClz().isArray()) {
                throw new ArrayStoreException();
            }
            System.arraycopy(src.getArrayDatas(), srcPos, desc.getArrayDatas(), descPos, length);
        }
    }
}
