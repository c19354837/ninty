package com.ninty.runtime;

/**
 * Created by ninty on 2017/7/12.
 */
public class Slot {
    int num;
    Object ref;

    @Override
    public String toString() {
        return "Slot{" + (ref == null ?
                "num=" + num
                : "ref=" + ref) +
                '}';
    }
}
