package com.ninty.runtime.heap.constantpool;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.ninty.runtime.heap.NiMethod;

/**
 * Created by ninty on 2017/7/24.
 */
public class MethodRef extends MemberRef {
    NiMethod method;

    public MethodRef(ConstantInfo.CPMemeber cp) {
        super(cp);
    }

    @Override
    protected void resolve() {
        // TODO
    }
}
