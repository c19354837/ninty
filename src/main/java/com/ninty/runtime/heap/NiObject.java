package com.ninty.runtime.heap;

import com.ninty.runtime.LocalVars;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiObject implements Cloneable {
    private NiClass clz;
    private LocalVars fields;
    private Object arrayDatas; // when it's array
    private Object extra;

    private Lock lock = new ReentrantLock();

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

    public NiClass getClzByExtra() {
        if (extra != null && extra instanceof NiClass) {
            return (NiClass) extra;
        }
        throw new UnsupportedOperationException("extra is not a NiClass: " + extra);
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

    public int getFieldInt(String name) {
        NiField field = clz.findField(name, "I");
        return fields.getInt(field.getSlotId());
    }

    public void setFieldRef(String name, String desc, NiObject ref) {
        NiField field = clz.findField(name, desc);
        fields.setRef(field.getSlotId(), ref);
    }

    public NiObject getFieldRef(String name, String desc) {
        NiField field = clz.findField(name, desc);
        return fields.getRef(field.getSlotId());
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        try {
            lock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread());
            System.exit(1);
        }
    }

    @Override
    public NiObject clone() throws CloneNotSupportedException {
        NiClassLoader loader = clz.getLoader();
        NiClass cloneable = loader.loadClass("java/lang/Cloneable");
        if (!clz.isImplements(cloneable)) {
            throw new CloneNotSupportedException();
        }

        NiObject cloneObj;
        switch (clz.getClassName().substring(0, 2)) {
            case "[Z":
            case "[B":
                cloneObj = new NiObject(clz, abyte().clone());
                break;
            case "[C":
                cloneObj = new NiObject(clz, achar().clone());
                break;
            case "[S":
                cloneObj = new NiObject(clz, ashort().clone());
                break;
            case "[I":
                cloneObj = new NiObject(clz, aint().clone());
                break;
            case "[J":
                cloneObj = new NiObject(clz, along().clone());
                break;
            case "[F":
                cloneObj = new NiObject(clz, afloat().clone());
                break;
            case "[D":
                cloneObj = new NiObject(clz, adouble().clone());
                break;
            case "[L":
                cloneObj = new NiObject(clz, aobject().clone());
                break;
            default:
                cloneObj = new NiObject(clz, fields.length());
                for (int i = 0; i < fields.length(); i++) {
                    // TODO: call slot's clone() if it implement Cloneable
                    cloneObj.fields.setSlot(i, fields.getSlot(i).duplicate());
                }
                break;
        }

        return cloneObj;
    }

    @Override
    public String toString() {
        return "NiObject{" + clz.className + '}';
    }
}
