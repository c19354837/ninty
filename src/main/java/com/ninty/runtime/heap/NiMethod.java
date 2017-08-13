package com.ninty.runtime.heap;

import com.ninty.classfile.AttributeInfo;
import com.ninty.classfile.MemberInfo;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiMethod extends ClassMember {
    private int maxLocals;
    private int maxStack;
    private int argsCount;

    private ByteBuffer codes;

    public NiMethod(NiClass clz, MemberInfo memberInfo) {
        copyMemberInfo(memberInfo);
        this.clz = clz;
        AttributeInfo.AttrCode attrCode = memberInfo.getAttrCode();
        if (attrCode != null) {
            maxLocals = attrCode.maxLocals;
            maxStack = attrCode.maxStack;
            codes = ByteBuffer.wrap(attrCode.codes);
        }
        argsCount = calcArgsCount();
        if (isNative()) {
            injectNativeCode(memberInfo.getDesc());
        }
    }

    private void injectNativeCode(String desc) {
        maxLocals = argsCount;
        maxStack = 4;
        char returnType = desc.charAt(desc.lastIndexOf(')') + 1);
        switch (returnType) {
            case 'V':
                codes = ByteBuffer.wrap(new byte[]{(byte) 0xfe, (byte) 0xb1});
                break;
            case 'D':
                codes = ByteBuffer.wrap(new byte[]{(byte) 0xfe, (byte) 0xaf});
                break;
            case 'F':
                codes = ByteBuffer.wrap(new byte[]{(byte) 0xfe, (byte) 0xae});
                break;
            case 'J':
                codes = ByteBuffer.wrap(new byte[]{(byte) 0xfe, (byte) 0xad});
                break;
            case 'L':
            case '[':
                codes = ByteBuffer.wrap(new byte[]{(byte) 0xfe, (byte) 0xb0});
                break;
            default:
                codes = ByteBuffer.wrap(new byte[]{(byte) 0xfe, (byte) 0xac}); //ireturn
                break;
        }
    }

    private int calcArgsCount() {
        int start = desc.indexOf('(');
        int end = desc.indexOf(')');
        if (start == -1 || end <= start) {
            throw new IllegalArgumentException("bad description for method" + this);
        }
        String args = desc.substring(start + 1, end);
        int argsCount = 0;
        for (int i = 0; i < args.length(); i++) {
            char arg = args.charAt(i);
            if (arg == '[') {
                continue;
            }
            argsCount++;
            if (arg == 'L') {
                i = args.indexOf(';', i);
            } else if (arg == 'J' || arg == 'D') {
                argsCount++;
            }
        }
        if (!isStatic()) {
            argsCount++;
        }
        return argsCount;
    }

    public boolean isAbstract() {
        return (accessFlags & ClassConstant.ACC_ABSTRACT) != 0;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getArgsCount() {
        return argsCount;
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
