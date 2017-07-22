package com.ninty.cmd.base;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/20.
 */
public abstract class DataCmd implements ICmdBase {
    protected ByteBuffer bb;
    private int index;

    @Override
    public void init(ByteBuffer bb) {
        this.bb = bb;
        index = bb.position() - 1;
    }

    public void jumpTo(int offset) {
        bb.position(index + offset);
    }
}
