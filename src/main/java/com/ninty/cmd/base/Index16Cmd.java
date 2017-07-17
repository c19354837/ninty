package com.ninty.cmd.base;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/16.
 */
public abstract class Index16Cmd implements ICmdBase {
    public int index;

    @Override
    public void init(ByteBuffer bb) {
        index = bb.getChar();
    }
}
