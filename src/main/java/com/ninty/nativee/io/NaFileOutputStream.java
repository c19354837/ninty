package com.ninty.nativee.io;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiObject;

/**
 * Created by ninty on 2017/10/21.
 */
public class NaFileOutputStream {
    private final static String className = "java/io/FileOutputStream";

    public static void init() {
        NaMethodManager.register(className, "initIDs", "()V",
                new initIDs());
        NaMethodManager.register(className, "writeBytes", "([BIIZ)V",
                new writeBytes());
    }

    public static class initIDs implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
        }
    }

    public static class writeBytes implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            LocalVars localVars = frame.getLocalVars();
            NiObject datas = localVars.getRef(1);
            int offset = localVars.getInt(2);
            int length = localVars.getInt(3);
            System.out.write(datas.abyte(), offset, length);
        }
    }
}
