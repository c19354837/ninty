package com.ninty.runtime;

import com.ninty.runtime.heap.NiObject;

import java.util.Arrays;

/**
 * Created by ninty on 2017/7/12.
 */
public class OperandStack {
    private int size;
    private Slot[] slots;

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

    public void pushFloat(float val) {
        int num = Float.floatToIntBits(val);
        pushInt(num);
    }

    public float popFloat() {
        int num = popInt();
        return Float.intBitsToFloat(num);
    }

    public void pushDouble(double val) {
        long num = Double.doubleToLongBits(val);
        pushLong(num);
    }

    public double popDouble() {
        long num = popLong();
        return Double.longBitsToDouble(num);
    }

    public void pushRef(NiObject ref) {
        slots[size].ref = ref;
        size++;
    }

    public NiObject popRef() {
        size--;
        NiObject ref = slots[size].ref;
        slots[size].ref = null;
        return ref;
    }

    public void pushSlot(Slot slot) {
        slots[size] = slot;
        size++;
    }

    public Slot popSlot() {
        size--;
        return slots[size];
    }

    public void clear() {
        while (size > 0){
            popSlot();
        }
    }

    public NiObject getRefFromTop(int n) {
        return slots[size - n].ref;
    }

    public NiObject getRefFromTop() {
        return getRefFromTop(1);
    }

    @Override
    public String toString() {
        return "OperandStack{" +
                "slots=" + Arrays.toString(slots) +
                ", size=" + size +
                '}';
    }
}
