package com.ninty.cmd.base;

import com.ninty.runtime.NiFrame;
import com.ninty.runtime.heap.CodeBytes;

/**
 * Created by ninty on 2017/7/16.
 */
public interface ICmdBase {

    void init(CodeBytes bb);

    void exec(NiFrame frame);
}
