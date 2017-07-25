package com.ninty.runtime.heap.constantpool;

import com.ninty.classfile.constantpool.ConstantInfo;

/**
 * Created by ninty on 2017/7/24.
 */
public abstract class MemberRef extends BaseSymbol {
    String name;
    String desc;

    public MemberRef(ConstantInfo.CPMemeber cp) {
        name = cp.name();
        desc = cp.desc();
    }
}
