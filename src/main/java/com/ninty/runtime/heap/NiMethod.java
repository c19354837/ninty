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
        if(attrCode != null){
            maxLocals = attrCode.maxLocals;
            maxStack = attrCode.maxStack;
            codes = ByteBuffer.wrap(attrCode.codes);
        }
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public ByteBuffer getCodes() {
        return codes;
    }

    @Override
    public String toString() {
        return "NiMethod{" +
                "clz=" + clz +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
