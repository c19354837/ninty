package com.ninty.cmd.base;

import com.ninty.runtime.heap.CodeBytes;

/**
 * Created by ninty on 2017/7/20.
 */
public abstract class DataCmd implements ICmdBase {
    protected CodeBytes bb;
    private int index;

    @Override
    public void init(CodeBytes bb) {
        this.bb = bb;
        index = bb.position() - 1;
    }

    public void jumpTo(int offset) {
        bb.position(index + offset);
    }
}
