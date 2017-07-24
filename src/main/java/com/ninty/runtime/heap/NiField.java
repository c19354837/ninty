package com.ninty.runtime.heap;

import com.ninty.classfile.MemberInfo;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiField extends ClassMember {
    public NiField(NiClass clz, MemberInfo memberInfo) {
        copy(memberInfo);
        this.clz = clz;
    }
}
