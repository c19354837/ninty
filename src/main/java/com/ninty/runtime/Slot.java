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

    public Slot duplicate() {
        if (ref == null) {
            return this; //only reference object need duplicate
        }
        Slot slot = new Slot();
        slot.ref = ref;
        return slot;
    }

    @Override
    public String toString() {
        return "Slot{" + (ref == null ? "num=" + num : "ref=" + ref) + '}';
    }
}
