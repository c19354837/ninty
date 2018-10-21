package com.ninty.nativee.sun.misc;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;

/**
 * Created by ninty on 2018/10/21
 */
public class NaURLClassPath {
    private final static String className = "sun/misc/URLClassPath";

    public static void init() {
        NaMethodManager.register(className, "getLookupCacheURLs", "(Ljava/lang/ClassLoader;)[Ljava/net/URL;",
                new getLookupCacheURLs());
    }

    // TODO
    public static class getLookupCacheURLs implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushRef(null);
        }
    }
}
