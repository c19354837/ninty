package com.ninty.runtime;

/**
 * Created by ninty on 2017/7/12.
 */
public class LocalVars {
    Slot[] slots;

    LocalVars(int maxLocalSize) {
        slots = new Slot[maxLocalSize];
    }

    int getInt(int index) {
        return slots[index].num;
    }

    void setInt(int index, int val) {
        slots[index].num = val;
    }

    float getFloat(int index) {
        int num = getInt(index);
        return Float.intBitsToFloat(num);
    }

    void setFloat(int index, float val){
        int num = Float.floatToIntBits(val);
        slots[index].num = num;
    }

    long getLong(int index){
        long low = (getInt(index) & 0x00000000ffffffffL) ;
        long high = (getInt(index+1) & 0x00000000ffffffffL) << 32;
        return high|low;
    }

    void setLong(int index, long val){
        int low = (int)val;
        int high = (int) (val>>>32);
        setInt(index, low);
        setInt(index, high);
    }
}
