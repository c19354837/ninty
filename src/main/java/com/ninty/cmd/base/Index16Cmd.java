package com.ninty.cmd.base;

import com.ninty.runtime.heap.CodeBytes;

/**
 * Created by ninty on 2017/7/16.
 */
public abstract class Index16Cmd implements ICmdBase {
    public int index;

    @Override
    public void init(CodeBytes bb) {
        index = bb.getChar();
    }
}
