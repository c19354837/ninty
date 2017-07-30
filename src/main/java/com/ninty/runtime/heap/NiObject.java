package com.ninty.runtime.heap;

import com.ninty.runtime.LocalVars;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiObject {
    private NiClass clz;
    private LocalVars fields;

    public NiObject(NiClass clz, int count) {
        this.clz = clz;
        fields = new LocalVars(count);
    }

    public NiObject(NiObject obj) {
        this.clz = obj.clz;
        this.fields = obj.fields;
    }

    public NiClass getClz() {
        return clz;
    }

    public LocalVars getFields() {
        return fields;
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
