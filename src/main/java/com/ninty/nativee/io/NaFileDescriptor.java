package com.ninty.nativee.io;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;

/**
 * Created by ninty on 2017/10/21.
 */
public class NaFileDescriptor {
    private final static String className = "java/io/FileDescriptor";

    public static void init() {
        NaMethodManager.register(className, "initIDs", "()V",
                new initIDs());
    }

    public static class initIDs implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
        }
    }
}
