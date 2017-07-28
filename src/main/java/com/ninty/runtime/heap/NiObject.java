package com.ninty.runtime.heap;

import com.ninty.runtime.LocalVars;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiObject {
    private NiClass clz;
    private LocalVars slots;

    public NiObject(NiClass clz, int count) {
        this.clz = clz;
        slots = new LocalVars(count);
    }

    public NiClass getClz() {
        return clz;
    }

    public LocalVars getSlots() {
        return slots;
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
