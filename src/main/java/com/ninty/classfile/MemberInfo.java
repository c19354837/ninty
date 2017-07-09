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
    private int attributeCount;
    private AttributeInfo[] attributeInfos;

    MemberInfo(ConstantPoolInfos cps, ByteBuffer bb){
        this.cps = cps;
        accessFlag = bb.getChar();
        nameIndex = bb.getChar();
        descIndex = bb.getChar();
        attributeCount = bb.getChar();
        attributeInfos = new AttributeInfo[attributeCount];
    }

    public String name(){
        return cps.getUtf8(nameIndex);
    }

    public String desc(){
        return cps.getUtf8(descIndex);
    }
}
