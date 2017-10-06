package com.ninty.nativee;

import com.ninty.nativee.lang.*;
import com.ninty.nativee.security.NaAccessController;
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
