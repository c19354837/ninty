package com.ninty.cmd.base;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/16.
 */
public abstract class BranchCmd implements ICmdBase {

    protected int offset;

    @Override
    public void init(ByteBuffer bb) {
        offset = bb.getShort();
    }
}
