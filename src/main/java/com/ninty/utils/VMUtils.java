package com.ninty.utils;

import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ninty on 2017/7/8.
 */
public class VMUtils {

    public static Map<String, String> primitiveTypes = new HashMap<>(16);

    static {
        primitiveTypes.put("void", "V");
        primitiveTypes.put("boolean", "Z");
        primitiveTypes.put("byte", "B");
        primitiveTypes.put("short", "S");
        primitiveTypes.put("char", "C");
        primitiveTypes.put("int", "I");
        primitiveTypes.put("long", "J");
        primitiveTypes.put("float", "F");
        primitiveTypes.put("double", "D");
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    public static int toUInt(byte val) {
        return val & 0xff;
    }

    public static String toClassname(String desc) {
        if (desc.charAt(0) == '[') {
            return desc;
        }
        if (desc.charAt(0) == 'L') {
            return desc.substring(1, desc.length() - 1);
        }
        for (String key : primitiveTypes.keySet()) {
            if (primitiveTypes.get(key).equals(desc)) {
                return key;
            }
        }
        throw new IllegalArgumentException("invalid descriptor:" + desc);
    }

    public static String[] toParams(String desc) {
        ArrayList<String> params = new ArrayList<>(8);
        int pos = 0;
        int end = 0;
        while (pos < desc.length()) {
            if (desc.charAt(pos) == '[') {
                if (desc.charAt(pos + 1) == 'L') {
                    end = desc.indexOf(';', pos) + 1;
                    params.add(desc.substring(pos, end - 1));
                } else {
                    end = pos + 2;
                    params.add(desc.substring(pos, end));
                }
            } else if (desc.charAt(pos) == 'L') {
                end = desc.indexOf(';', pos + 1) + 1;
                params.add(desc.substring(pos + 1, end - 1));
            } else {
                String val = desc.substring(pos, pos + 1);
                for (String key : primitiveTypes.keySet()) {
                    if (primitiveTypes.get(key).equals(val)) {
                        end = pos + 1;
                        params.add(key);
                        break;
                    }
                }
            }
            pos = end;
        }
        return params.toArray(new String[params.size()]);
    }

    public static String toDesc(NiObject params, String returnType) {
        StringBuilder desc = new StringBuilder("(");
        NiObject[] clzs = params.aobject();
        for (int i = 0; i < clzs.length; i++) {
            NiClass clz = clzs[i].getClzByExtra();
            desc.append(clz.getClassName());
        }
        desc.append(')');
        desc.append(returnType);
        return desc.toString();
    }

    public static String normalizeClassName(String className) {
        return className.replace('.', '/');
    }
}
