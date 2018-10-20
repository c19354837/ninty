package com.ninty.nativee;

import com.ninty.nativee.io.NaFileDescriptor;
import com.ninty.nativee.io.NaFileInputStream;
import com.ninty.nativee.io.NaFileOutputStream;
import com.ninty.nativee.lang.*;
import com.ninty.nativee.lang.reflect.NaArray;
import com.ninty.nativee.lang.invoke.NaMethodHandleNatives;
import com.ninty.nativee.security.NaAccessController;
import com.ninty.nativee.sun.misc.NaUnsafe;
import com.ninty.nativee.sun.misc.NaVM;
import com.ninty.nativee.sun.reflect.NaConstantPool;
import com.ninty.nativee.sun.reflect.NaNativeConstructorAccessorImpl;
import com.ninty.nativee.sun.reflect.NaReflection;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ninty on 2017/8/13.
 */
public class NaMethodManager {

    private static Map<String, INativeMethod> methodMap = new HashMap<>(1 << 10);

    // TODO refactor
    static {
        NaObject.init();
        NaClass.init();
        NaSystem.init();
        NaFloat.init();
        NaDouble.init();
        NaString.init();
        NaThrowable.init();
        NaThread.init();
        NaAccessController.init();
        NaReflection.init();
        NaVM.init();
        NaFileInputStream.init();
        NaUnsafe.init();
        NaFileDescriptor.init();
        NaFileOutputStream.init();
        NaConstantPool.init();
        NaMethodHandleNatives.init();
        NaRuntime.init();
        NaArray.init();
        NaNativeConstructorAccessorImpl.init();
    }

    private final static INativeMethod EMPTY = (NiFrame frame) -> {
    };

    public static void register(String className, String name, String desc, INativeMethod nativeMethod) {
        methodMap.put(getKey(className, name, desc), nativeMethod);
    }

    public static INativeMethod findNativeMethod(NiMethod method) {
        String key = getKey(method);
        INativeMethod nativeMethod = methodMap.get(key);
        if (nativeMethod != null) {
            return nativeMethod;
        }

        if (method.getDesc().equals("()V") && method.getName().equals("registerNatives")) {
            return EMPTY;
        }
        return null;
    }

    private static String getKey(NiMethod method) {
        NiClass clz = method.getClz();
        String className = clz.getClassName();
        String methodName = method.getName();
        String methodDesc = method.getDesc();
        return className + '~' + methodName + '~' + methodDesc;
    }

    private static String getKey(String className, String name, String desc) {
        return className + '~' + name + '~' + desc;
    }
}
