package com.ninty.runtime.heap;

import com.ninty.classfile.AttributeInfo;
import com.ninty.classfile.MemberInfo;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiMethod extends ClassMember {
    int maxLocals;
    int maxStack;
    ByteBuffer codes;

    public NiMethod(NiClass clz, MemberInfo memberInfo) {
        copyMemberInfo(memberInfo);
        this.clz = clz;
        AttributeInfo.AttrCode attrCode = memberInfo.getAttrCode();
        maxLocals = attrCode.maxLocals;
        maxStack = attrCode.maxStack;
        codes = ByteBuffer.wrap(attrCode.codes);
    }
}
