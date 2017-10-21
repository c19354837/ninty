package com.ninty.cmd.base;

import com.ninty.runtime.heap.CodeBytes;

import static com.ninty.utils.VMUtils.toUInt;

/**
 * Created by ninty on 2017/7/16.
 */
public abstract class Index8Cmd implements ICmdBase {

    public int index;

    @Override
    public void init(CodeBytes bb) {
        index = toUInt(bb.get());
    }
}
