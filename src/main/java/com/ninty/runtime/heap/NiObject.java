package com.ninty.runtime.heap;

import com.ninty.runtime.LocalVars;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiObject {
    private NiClass clz;
    private LocalVars fields;
    private Object arrayDatas; // when it's array

    public NiObject(NiClass clz, int count) {
        this.clz = clz;
        fields = new LocalVars(count);
    }

    public NiObject(NiObject obj) {
        this.clz = obj.clz;
        this.fields = obj.fields;
        this.arrayDatas = obj.arrayDatas;
    }

    public NiObject(NiClass clz, Object arrayDatas) {
        this.clz = clz;
        this.arrayDatas = arrayDatas;
    }

    public NiClass getClz() {
        return clz;
    }

    public LocalVars getFields() {
        return fields;
    }

    public Object getArrayDatas() {
        return arrayDatas;
    }

    public byte[] abyte() {
        return (byte[]) arrayDatas;
    }

    public short[] ashort() {
        return (short[]) arrayDatas;
    }

    public char[] achar() {
        return (char[]) arrayDatas;
    }

    public int[] aint() {
        return (int[]) arrayDatas;
    }

    public long[] along() {
        return (long[]) arrayDatas;
    }

    public float[] afloat() {
        return (float[]) arrayDatas;
    }

    public double[] adouble() {
        return (double[]) arrayDatas;
    }

    public NiObject[] aobject() {
        return (NiObject[]) arrayDatas;
    }

    public int arrayLength() {
        Class clz = arrayDatas.getClass();
        if (clz == byte[].class) {
            return abyte().length;
        } else if (clz == short[].class) {
            return ashort().length;
        } else if (clz == char[].class) {
            return achar().length;
        } else if (clz == int[].class) {
            return aint().length;
        } else if (clz == long[].class) {
            return along().length;
        } else if (clz == float[].class) {
            return afloat().length;
        } else if (clz == double[].class) {
            return adouble().length;
        } else if (clz == NiObject[].class) {
            return aobject().length;
        }
        throw new IllegalAccessError("Only array can access length");
    }

    public boolean isInstanceOf(NiClass clz) {
        return clz.isAssignableFrom(this.clz);
    }

    @Override
    public String toString() {
        return "NiObject{" +
                clz.className +
                '}';
    }
}
