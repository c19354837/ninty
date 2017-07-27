package com.ninty.runtime.heap.constantpool;

import com.ninty.classfile.constantpool.ConstantInfo;

/**
 * Created by ninty on 2017/7/24.
 */
public class ClassRef extends BaseSymbol {
    public ClassRef(ConstantInfo.CPClass cp) {
        className = cp.className();
    }

    @Override
    public void resolve() {
        resolveClass();
    }
}
