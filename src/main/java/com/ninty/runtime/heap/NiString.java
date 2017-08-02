package com.ninty.runtime.heap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ninty on 2017/8/2.
 */
public class NiString {

    private static Map<String, NiObject> stringPool = new HashMap<>(1 << 10);
    private static String className = "java/lang/String";

    public static NiObject newString(NiClassLoader loader, String str) {
        if (stringPool.containsKey(str)) {
            return stringPool.get(str);
        }
        char[] chars = str.toCharArray();
        NiObject value = new NiObject(loader.loadClass("[C"), chars);
        NiObject strRef = loader.loadClass(className).newObject();
        NiField field = strRef.getClz().findField("value", "[C");
        strRef.getFields().setRef(field.getSlotId(), value);
        stringPool.put(str, strRef);
        return strRef;
    }

    public static String getString(NiObject ref) {
        NiField field = ref.getClz().findField("value", "[C");
        NiObject value = ref.getFields().getRef(field.getSlotId());
        return new String((char[]) value.getArrayDatas());
    }
}
