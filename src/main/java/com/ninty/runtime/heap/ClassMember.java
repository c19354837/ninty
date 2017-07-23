package com.ninty.runtime.heap;

import com.ninty.classfile.MemberInfo;

/**
 * Created by ninty on 2017/7/23.
 */
public class ClassMember {

    int accessFlags;
    String name;
    String desc;
    NiClass clz;

    void copy(MemberInfo memberInfo) {
        accessFlags = memberInfo.getAccessFlag();
        name = memberInfo.getName();
        desc = memberInfo.getDesc();
    }
}
