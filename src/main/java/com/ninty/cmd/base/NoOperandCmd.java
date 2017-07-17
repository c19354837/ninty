package com.ninty.cmd.base;

import com.ninty.runtime.NiFrame;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/16.
 */
public abstract class NoOperandCmd implements ICmdBase {
    @Override
    public void init(ByteBuffer bb) {

    }

    @Override
    public void exec(NiFrame frame) {

    }
}
