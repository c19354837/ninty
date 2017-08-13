package com.ninty.nativee;

import com.ninty.runtime.NiFrame;

/**
 * Created by ninty on 2017/8/13.
 */
public interface INativeMethod {
    void invoke(NiFrame frame);
}
