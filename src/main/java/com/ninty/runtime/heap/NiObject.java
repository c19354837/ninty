package com.ninty.runtime.heap;

import com.ninty.runtime.LocalVars;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiObject {
    private NiClass clz;
    private LocalVars fields;
    private Object arrayDatas; // when it's array
    private Object extra;

    private Thread lockThread;
    private Map<Thread, Integer> waitList = new HashMap<>();

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

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
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

    // this.clz instanceof clz
    public boolean isInstanceOf(NiClass clz) {
        return clz.isAssignableFrom(this.clz);
    }

    public void setFieldInt(String name, int val) {
        NiField field = clz.findField(name, "I");
        fields.setInt(field.getSlotId(), val);
    }

    public void setFieldRef(String name, String desc, NiObject ref) {
        NiField field = clz.findField(name, desc);
        fields.setRef(field.getSlotId(), ref);
    }

    public NiObject getFieldRef(String name, String desc) {
        NiField field = clz.findField(name, desc);
        return fields.getRef(field.getSlotId());
    }

    synchronized public void lock() {
        Thread thread = Thread.currentThread();
        if (waitList.containsKey(thread)) {
            waitList.put(thread, waitList.get(thread) + 1);
        } else {
            waitList.put(thread, 1);
        }
        if (waitList.size() > 1 && waitList.get(thread) == 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public void unlock() {
        Thread thread = Thread.currentThread();
        int count = waitList.get(thread);
        boolean release = false;
        if(count > 1){
            waitList.put(thread, count -1);
        }else{
            waitList.remove(thread);
            release = true;
        }
        if (release) {
            notifyAll();
        }
    }

    @Override
    public String toString() {
        return "NiObject{" + clz.className + '}';
    }
}
