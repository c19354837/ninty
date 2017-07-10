package com.ninty.classfile;

import com.ninty.classfile.constantpool.ConstantPoolInfos;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/9.
 */
public class MemberInfo {

    private ConstantPoolInfos cps;
    private int accessFlag;
    private int nameIndex;
    private int descIndex;
    private AttributeInfo[] attributeInfos;

    MemberInfo(ConstantPoolInfos cps, ByteBuffer bb){
        this.cps = cps;
        accessFlag = bb.getChar();
        nameIndex = bb.getChar();
        descIndex = bb.getChar();
        fillAttributes(cps, bb);
    }

    public String name(){
        return cps.getUtf8(nameIndex);
    }

    public String desc(){
        return cps.getUtf8(descIndex);
    }

    private void fillAttributes(ConstantPoolInfos cps, ByteBuffer bb){
        int count = bb.getChar();
        attributeInfos = new AttributeInfo[count];
        for (int i = 0; i < count; i++) {

        }
    }
}
