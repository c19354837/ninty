package com.ninty.classfile.constantpool;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/9.
 */
public class ConstantPoolInfos {

    private ConstantInfo[] cps;

    public ConstantPoolInfos(ByteBuffer bb) {
        int len = bb.getChar();
        cps = new ConstantInfo[len - 1];

        for (int i = 0; i < cps.length; i++) {
            ConstantInfo cp = ConstantInfo.generate(this, bb);
            cps[i] = cp;
            if (cp instanceof ConstantInfo.CPFLong || cp instanceof ConstantInfo.CPDouble) {
                i++;
            }
        }
    }

    public ConstantInfo get(int index) {
        ConstantInfo ci = cps[index];
        if (ci == null) {
            throw new NullPointerException("cannot find constant index:" + index);
        }
        return ci;
    }

    public String getUtf8(int index) {
        return ((ConstantInfo.CPUtf8)get(index)).value;
    }
}

