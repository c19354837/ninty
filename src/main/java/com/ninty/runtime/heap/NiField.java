package com.ninty.runtime.heap;

import com.ninty.classfile.AttributeInfo;
import com.ninty.classfile.MemberInfo;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiField extends ClassMember {

    int slotId;
    int constantValueIndex;

    NiField(NiClass clz, MemberInfo memberInfo) {
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

    boolean isLongOrDouble() {
        return desc.equals("J") || desc.equals("D");
    }

    public int getSlotId() {
        return slotId;
    }

    public NiClass getType() {
        switch (desc.charAt(0)) {
            case 'B':
                return clz.loader.loadClass("byte");
            case 'C':
                return clz.loader.loadClass("char");
            case 'D':
                return clz.loader.loadClass("double");
            case 'F':
                return clz.loader.loadClass("float");
            case 'I':
                return clz.loader.loadClass("int");
            case 'J':
                return clz.loader.loadClass("long");
            case 'S':
                return clz.loader.loadClass("short");
            case 'Z':
                return clz.loader.loadClass("boolean");
            case 'V':
                return clz.loader.loadClass("void");
            case '[':
                return clz.getLoader().loadClass(desc);
            case 'L':
                return clz.getLoader().loadClass(desc.substring(1, desc.length() - 1));
            default:
                throw new UnsupportedOperationException("unkonw type:" + desc);
        }
    }
}
