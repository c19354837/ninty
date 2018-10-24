package com.ninty.nativee.lang;

import com.ninty.nativee.INativeMethod;
import com.ninty.nativee.NaMethodManager;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.Slot;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiMethod;
import com.ninty.runtime.heap.NiObject;
import com.ninty.runtime.heap.NiString;

import java.util.Properties;

/**
 * Created by ninty on 2017/8/19.
 */
public class NaSystem {

    private final static String className = "java/lang/System";

    public static void init() {
        NaMethodManager.register(className, "arraycopy", "(Ljava/lang/Object;ILjava/lang/Object;II)V", new arraycopy());
        NaMethodManager.register(className, "initProperties", "(Ljava/util/Properties;)Ljava/util/Properties;", new initProperties());
        NaMethodManager.register(className, "nanoTime", "()J", new nanoTime());
        NaMethodManager.register(className, "setIn0", "(Ljava/io/InputStream;)V", new setIn0());
        NaMethodManager.register(className, "setOut0", "(Ljava/io/PrintStream;)V", new setOut0());
        NaMethodManager.register(className, "setErr0", "(Ljava/io/PrintStream;)V", new setErr0());
        NaMethodManager.register(className, "mapLibraryName", "(Ljava/lang/String;)Ljava/lang/String;", new mapLibraryName());
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

    public static class initProperties implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject ref = frame.getLocalVars().getRef(0);
            frame.getOperandStack().pushRef(ref);

            NiMethod setProperty = ref.getClz().getMethod("setProperty", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
            Properties properties = System.getProperties();
            for (String key : properties.stringPropertyNames()) {
                NiThread.execMethodAtCurrent(frame, setProperty,
                        new Slot(ref),
                        new Slot(NiString.newString(ref.getClz().getLoader(), key)),
                        new Slot(NiString.newString(ref.getClz().getLoader(), properties.getProperty(key))));
            }
        }
    }

    public static class nanoTime implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            frame.getOperandStack().pushLong(System.nanoTime());
        }
    }

    public static class setIn0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            NiClass clz = frame.getMethod().getClz();
            clz.setStaticRef("in", "Ljava/io/InputStream;", self);
        }
    }

    public static class setOut0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            NiClass clz = frame.getMethod().getClz();
            clz.setStaticRef("out", "Ljava/io/PrintStream;", self);
        }
    }

    public static class setErr0 implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            NiObject self = frame.getLocalVars().getThis();
            NiClass clz = frame.getMethod().getClz();
            clz.setStaticRef("err", "Ljava/io/PrintStream;", self);
        }
    }

    public static class mapLibraryName implements INativeMethod {
        @Override
        public void invoke(NiFrame frame) {
            String libname = NiString.getString(frame.getLocalVars().getRef(0));
            String result = System.mapLibraryName(libname);
            frame.getOperandStack().pushRef(NiString.newString(frame.getMethod().getClz().getLoader(), result));
        }
    }
}
