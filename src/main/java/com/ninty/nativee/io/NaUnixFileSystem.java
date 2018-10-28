package com.ninty.nativee.io;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiClassLoader;
import com.ninty.runtime.heap.NiObject;
import com.ninty.runtime.heap.NiString;

import java.io.File;

/**
 * Created by ninty on 2018/10/24
 */
public class NaUnixFileSystem {
    private final static String className = "java/io/UnixFileSystem";

    public static void init() {
        NaMethodManager.register(className, "initIDs", "()V",
                new initIDs());
        NaMethodManager.register(className, "getBooleanAttributes0", "(Ljava/io/File;)I",
                new getBooleanAttributes0());
        NaMethodManager.register(className, "list", "(Ljava/io/File;)[Ljava/lang/String;",
                new list());
        NaMethodManager.register(className, "canonicalize0", "(Ljava/lang/String;)Ljava/lang/String;",
                new canonicalize0());
    }

    public static class initIDs implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
        }
    }

    public static class getBooleanAttributes0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushInt(0);
        }
    }

    public static class list implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject fileObj = frame.getLocalVars().getRef(1);
            String path = NiString.getString(fileObj.getFieldRef("path", "Ljava/lang/String;"));
            File file = new File(path);
            String[] files = file.list();
            if (files == null) {
                frame.getOperandStack().pushRef(null);
                return;
            }
            NiClassLoader loader = frame.getMethod().getClz().getLoader();
            NiClass arrStr = loader.loadClass("[java/lang/String;");
            NiObject filesObj = arrStr.newArray(files.length);
            for (int i = 0; i < files.length; i++) {
                filesObj.aobject()[i] = NiString.newString(loader, files[i]);
            }
            frame.getOperandStack().pushRef(filesObj);
        }
    }

    public static class canonicalize0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject path = frame.getLocalVars().getRef(1);
            frame.getOperandStack().pushRef(path);
        }
    }
}
