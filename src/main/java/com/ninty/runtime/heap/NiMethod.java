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
        return argsCount;
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
