package com.ninty.runtime;

import com.ninty.runtime.heap.NiObject;

/**
 * Created by ninty on 2017/7/12.
 */
public class Slot {
    int num;
    NiObject ref;

    public Slot() {
    }

    public Slot(NiObject ref) {
        this.ref = ref;
    }

    public Slot(int num) {
        this.num = num;
    }

    public Slot duplicate() {
        Slot slot = new Slot();
        slot.num = num;
        slot.ref = ref;
        return slot;
    }

    public int getNum() {
        return num;
    }

    public NiObject getRef() {
        return ref;
    }

    @Override
    public String toString() {
        return "Slot{" + (ref == null ? "num=" + num : "ref=" + ref) + '}';
    }
}
