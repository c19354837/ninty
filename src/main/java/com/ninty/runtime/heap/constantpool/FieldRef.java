package com.ninty.runtime.heap.constantpool;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.ninty.runtime.heap.NiField;

/**
 * Created by ninty on 2017/7/24.
 */
public class FieldRef extends MemberRef {
    NiField field;

    public FieldRef(ConstantInfo.CPMemeber cp) {
        super(cp);
    }
}
