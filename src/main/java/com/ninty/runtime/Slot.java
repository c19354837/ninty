package com.ninty.runtime;

import com.ninty.runtime.heap.NiObject;

/**
 * Created by ninty on 2017/7/12.
 */
public class Slot {
    int num;
    NiObject ref;

    @Override
    public String toString() {
        return "Slot{" + (ref == null ?
                "num=" + num
                : "ref=" + ref) +
                '}';
    }
}
