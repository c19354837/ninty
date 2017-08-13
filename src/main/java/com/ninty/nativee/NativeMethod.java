package com.ninty.nativee;

import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ninty on 2017/8/13.
 */
public class NativeMethod {

    private final static NativeMethod EMPTY = new NativeMethod();

    public static Map<String, NativeMethod> methodMap = new HashMap<>(1<<10);

    public static void register(NiMethod method, NativeMethod nativeMethod){
        methodMap.put(getKey(method), nativeMethod);
    }

    public static NativeMethod findNativeMethod(NiMethod method){
        String key = getKey(method);
        NativeMethod nativeMethod = methodMap.get(key);
        if(nativeMethod != null){
            return nativeMethod;
        }

        if(method.getDesc().equals("()V") && method.getName().equals("registerNatives")){
            return EMPTY;
        }
        return null;
    }

    private static String getKey(NiMethod method){
        NiClass clz = method.getClz();
        String className = clz.getClassName();
        String methodName = method.getName();
        String methodDesc = method.getDesc();
        return className + '~' + methodName + '~' + methodDesc;
    }
}
