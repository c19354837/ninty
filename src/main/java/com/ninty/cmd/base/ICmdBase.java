package com.ninty.cmd.base;

import com.ninty.runtime.NiFrame;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/16.
 */
public interface ICmdBase {

    void init(ByteBuffer bb);

    void exec(NiFrame frame);
}
