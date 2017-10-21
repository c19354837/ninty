package com.ninty.cmd.base;

import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.CodeBytes;

/**
 * Created by ninty on 2017/7/16.
 */
public abstract class NoOperandCmd implements ICmdBase {
    @Override
    public void init(CodeBytes bb) {

    }

    @Override
    public void exec(NiFrame frame) {

    }
}
