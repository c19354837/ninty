package com.ninty.runtime.heap;

import com.ninty.classfile.AttributeInfo;
import com.ninty.classfile.MemberInfo;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiField extends ClassMember {

    int slotId;
    int constantValueIndex;

    public NiField(NiClass clz, MemberInfo memberInfo) {
        copyMemberInfo(memberInfo);
        copyAttr(memberInfo);
        this.clz = clz;
    }

    private void copyAttr(MemberInfo memberInfo) {
        AttributeInfo.AttrConstantValue constantValue = memberInfo.getAttrConstantValue();
        if (constantValue != null) {
            constantValueIndex = constantValue.getConstantValueIndex();
        }
    }

    public boolean isLongOrDouble() {
        return desc.equals("J") || desc.equals("D");
    }

    public int getSlotId() {
        return slotId;
    }
}
