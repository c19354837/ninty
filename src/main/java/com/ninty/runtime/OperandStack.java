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

    public void pushInt(int val) {
        slots[size].num = val;
        size++;
    }

    public int popInt() {
        size--;
        return slots[size].num;
    }

    public void pushLong(long val) {
        int low = (int) val;
        int high = (int) (val >>> 32);
        pushInt(low);
        pushInt(high);
    }

    public long popLong() {
        long high = (popInt() & 0x00000000ffffffffL) << 32;
        long low = popInt() & 0x00000000ffffffffL;
        return high | low;
    }

    public void pushFloat(float val){
        int num = Float.floatToIntBits(val);
        pushInt(num);
    }

    public float popFloat(){
        int num = popInt();
        return Float.intBitsToFloat(num);
    }

    public void pushDouble(double val){
        long num = Double.doubleToLongBits(val);
        pushLong(num);
    }

    public double popDouble(){
        long num = popLong();
        return Double.longBitsToDouble(num);
    }

    public void pushRef(Object ref){
        slots[size].ref = ref;
        size++;
    }

    public Object popRef(){
        size--;
        Object ref = slots[size].ref;
        slots[size].ref = null;
        return ref;
    }
}