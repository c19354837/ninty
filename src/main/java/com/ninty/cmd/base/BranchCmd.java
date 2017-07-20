package com.ninty.cmd.base;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/16.
 */
public abstract class BranchCmd extends DataCmd {

    private int offset;

    @Override
    public void init(ByteBuffer bb) {
        super.init(bb);
        offset = bb.getShort();
    }

    protected void branch() {
        jumpTo(offset);
    }
}
