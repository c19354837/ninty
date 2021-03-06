package com.ninty.classfile;

import com.ninty.classfile.constantpool.ConstantPoolInfos;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.ninty.classfile.AttributeInfo.*;

/**
 * Created by ninty on 2017/7/9.
 */
public class MemberInfo {

    private ConstantPoolInfos cps;
    private int accessFlag;
    private String name;
    private String desc;
    private AttributeInfo[] attributeInfos;

    MemberInfo(ConstantPoolInfos cps, ByteBuffer bb) {
        this.cps = cps;
        accessFlag = bb.getChar();
        name = cps.getUtf8(bb);
        desc = cps.getUtf8(bb);
        fillAttributes(cps, bb);
    }

    private void fillAttributes(ConstantPoolInfos cps, ByteBuffer bb) {
        int count = bb.getChar();
        attributeInfos = new AttributeInfo[count];
        for (int i = 0; i < count; i++) {
            attributeInfos[i] = AttributeInfo.generate(cps, bb);
        }
    }

    public int getAccessFlag() {
        return accessFlag;
    }

    public AttrCode getAttrCode() {
        for (AttributeInfo attr : attributeInfos) {
            if (attr instanceof AttrCode) {
                return (AttrCode) attr;
            }
        }
        return null;
    }

    public AttrConstantValue getAttrConstantValue() {
        for (AttributeInfo attr : attributeInfos) {
            if (attr instanceof AttrConstantValue) {
                return (AttrConstantValue) attr;
            }
        }
        return null;
    }

    public String getAttrSignature() {
        for (AttributeInfo attr : attributeInfos) {
            if (attr instanceof Signature) {
                return ((Signature) attr).signature;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "MemberInfo{" + "accessFlag=" + accessFlag + ", name='" + name + '\'' + ", desc='" + desc + '\'' + ", "
                + "attributeInfos=" + Arrays.toString(attributeInfos) + '}';
    }
}
