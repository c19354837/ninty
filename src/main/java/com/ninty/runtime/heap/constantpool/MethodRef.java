package com.ninty.runtime.heap.constantpool;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.ninty.runtime.heap.NiMethod;

/**
 * Created by ninty on 2017/7/24.
 */
public class MethodRef extends MemberRef {
    private NiMethod method;

    public MethodRef(ConstantInfo.CPMemeber cp) {
        super(cp);
    }

    @Override
    public void resolve() {
        // TODO
    }

    public NiMethod getMethod() {
        return method;
    }
}
