package com.ninty.cmd.base;

import java.nio.ByteBuffer;

import static com.ninty.utils.VMUtils.toUInt;

/**
 * Created by ninty on 2017/7/16.
 */
public abstract class Index8Cmd implements ICmdBase {

    public int index;

    @Override
    public void init(ByteBuffer bb) {
        index = toUInt(bb.get());
    }
}
