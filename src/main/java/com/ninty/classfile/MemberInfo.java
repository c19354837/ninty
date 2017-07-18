package com.ninty.classfile;

import com.ninty.classfile.constantpool.ConstantPoolInfos;

import java.nio.ByteBuffer;
import java.util.Arrays;

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
        name = cps.getUtf8(bb.getChar());
        desc = cps.getUtf8(bb.getChar());
        fillAttributes(cps, bb);
    }

    private void fillAttributes(ConstantPoolInfos cps, ByteBuffer bb) {
        int count = bb.getChar();
        attributeInfos = new AttributeInfo[count];
        for (int i = 0; i < count; i++) {
            attributeInfos[i] = AttributeInfo.generate(cps, bb);
        }
    }

    @Override
    public String toString() {
        return "MemberInfo{" +
                "accessFlag=" + accessFlag +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", attributeInfos=" + Arrays.toString(attributeInfos) +
                '}';
    }
}
