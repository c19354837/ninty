/*
 * MIT License
 *
 * Copyright (c) 2017 ninty
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ninty.nativee;

import com.ninty.runtime.LocalVars;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiClassLoader;
import com.ninty.runtime.heap.NiField;
import com.ninty.runtime.heap.NiObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ninty on 2017/9/25.
 */
public class NintyJavaCache {

    private static final Map<Object, NiObject> cache = new HashMap<>(1 << 10);

    private static NiClassLoader classLoader;

    /**
     * convert Object to NiObject, then cache it.
     */
    public static NiObject cache(Object obj) {
        if(obj == null){
            return null;
        }

        NiClass clz = classLoader.loadClass(obj.getClass().getName());
        NiObject ref = clz.newObject();

        try {
            fillField(obj, ref);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return ref;
    }

    private static void fillField(Object obj, NiObject ref) throws NoSuchFieldException, IllegalAccessException {
        Class<?> objClass = obj.getClass();

        LocalVars refFieldVals = ref.getFields();

        NiField[] refFields = ref.getClz().getFields();
        for (int i = 0; i < refFields.length; i++) {
            NiField refField = refFields[i];
            Field declaredField = objClass.getDeclaredField(refField.getName());
            Class<?> type = declaredField.getType();
            if(type == boolean.class){
                refFieldVals.setInt(refField.getSlotId(), declaredField.getBoolean(obj) ? 1 : 0);
            }else if(type == byte.class){
                refFieldVals.setInt(refField.getSlotId(), declaredField.getByte(obj));
            }else if(type == short.class){
                refFieldVals.setInt(refField.getSlotId(), declaredField.getShort(obj));
            }else if(type == char.class){
                refFieldVals.setInt(refField.getSlotId(), declaredField.getChar(obj));
            }else if(type == int.class){
                refFieldVals.setInt(refField.getSlotId(), declaredField.getInt(obj));
            }else if(type == float.class){
                refFieldVals.setFloat(refField.getSlotId(), declaredField.getFloat(obj));
            }else if(type == long.class){
                refFieldVals.setLong(refField.getSlotId(), declaredField.getLong(obj));
            }else if(type == double.class){
                refFieldVals.setDouble(refField.getSlotId(), declaredField.getDouble(obj));
            }
        }
    }

    public static void setClassLoader(NiClassLoader classLoader) {
        NintyJavaCache.classLoader = classLoader;
    }
}
