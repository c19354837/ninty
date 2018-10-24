package com.ninty.nativee.io;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;

/**
 * Created by ninty on 2018/10/24
 */
public class NaUnixFileSystem {
    private final static String className = "java/io/UnixFileSystem";

    public static void init() {
        NaMethodManager.register(className, "initIDs", "()V",
                new NaFileOutputStream.initIDs());
    }

    public static class initIDs implements INativeMethod{
        @Override
        public void invoke(NiFrame frame) {
        }
    }
}
