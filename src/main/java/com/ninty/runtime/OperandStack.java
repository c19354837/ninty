package com.ninty.runtime;

/**
 * Created by ninty on 2017/7/12.
 */
public class OperandStack {
    int size;
    Slot[] slots;

    OperandStack(int size) {
        slots = new Slot[size];
        for (int i = 0; i < size; i++) {
            slots[i] = new Slot();
        }
    }

    void pushInt(int val) {
        slots[size].num = val;
        size++;
    }

    int popInt() {
        size--;
        return slots[size].num;
    }

    void pushLong(long val) {
        int low = (int) val;
        int high = (int) (val >>> 32);
        pushInt(low);
        pushInt(high);
    }

    long popLong() {
        long high = (popInt() & 0x00000000ffffffffL) << 32;
        long low = popInt() & 0x00000000ffffffffL;
        return high | low;
    }

    void pushFloat(float val){
        int num = Float.floatToIntBits(val);
        pushInt(num);
    }

    float popFloat(){
        int num = popInt();
        return Float.intBitsToFloat(num);
    }

    void pushDouble(double val){
        long num = Double.doubleToLongBits(val);
        pushLong(num);
    }

    double popDouble(){
        long num = popLong();
        return Double.longBitsToDouble(num);
    }

    void pushRef(Object ref){
        slots[size].ref = ref;
        size++;
    }

    Object popRef(){
        size--;
        Object ref = slots[size].ref;
        slots[size].ref = null;
        return ref;
    }
}
