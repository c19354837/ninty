package com.ninty.runtime;

/**
 * Created by ninty on 2017/7/12.
 */
public class LocalVars {
    Slot[] slots;

    LocalVars(int maxLocalSize) {
        slots = new Slot[maxLocalSize];
        for (int i = 0; i < maxLocalSize; i++) {
            slots[i] = new Slot();
        }
    }

    public int getInt(int index) {
        return slots[index].num;
    }

    public void setInt(int index, int val) {
        slots[index].num = val;
    }

    public long getLong(int index){
        long low = (getInt(index) & 0x00000000ffffffffL) ;
        long high = (getInt(index+1) & 0x00000000ffffffffL) << 32;
        return high|low;
    }

    public void setLong(int index, long val){
        int low = (int)val;
        int high = (int) (val>>>32);
        setInt(index, low);
        setInt(index+1, high);
    }

    public float getFloat(int index) {
        int num = getInt(index);
        return Float.intBitsToFloat(num);
    }

    public void setFloat(int index, float val){
        int num = Float.floatToIntBits(val);
        setInt(index, num);
    }

    public double getDouble(int index){
        long num = getLong(index);
        return Double.longBitsToDouble(num);
    }

    void setDouble(int index, double val){
        long num = Double.doubleToLongBits(val);
        setLong(index, num);
    }

    public Object getRef(int index){
        return slots[index].ref;
    }

    public void setRef(int index, Object ref){
        slots[index].ref = ref;
    }
}
