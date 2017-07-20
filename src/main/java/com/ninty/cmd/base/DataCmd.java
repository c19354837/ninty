package com.ninty.cmd.base;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/20.
 */
public abstract class DataCmd implements ICmdBase {
    protected ByteBuffer bb;

    @Override
    public void init(ByteBuffer bb) {
        this.bb = bb;
    }

    public void jumpTo(int offset) {
        bb.position(bb.position() + offset);
    }
}
