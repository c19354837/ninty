package com.ninty.classfile.constantpool;

import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/9.
 */
public class ConstantPoolInfos {

    private ConstantInfo[] cps;

    public ConstantPoolInfos(ByteBuffer bb) {
        int len = bb.getChar();
        cps = new ConstantInfo[len];

        for (int i = 1; i < len; i++) {
            ConstantInfo cp = ConstantInfo.generate(this, bb);
            cps[i] = cp;
            if (cp instanceof ConstantInfo.CPLong || cp instanceof ConstantInfo.CPDouble) {
                i++;
            }
        }
    }

    ConstantInfo get(int index) {
        ConstantInfo ci = cps[index];
        if (ci == null) {
            throw new NullPointerException("cannot find constant index:" + index);
        }
        return ci;
    }

    public ConstantInfo[] getCps() {
        return cps;
    }

    public String getClassName(int index) {
        return ((ConstantInfo.CPClass) get(index)).className();
    }

    public String getSuperClassName(int index) {
        //java.lang.Object has no super class
        return index == 0 ? "" : getClassName(index);
    }

    public String getUtf8(int index) {
        return ((ConstantInfo.CPUtf8) get(index)).value();
    }
}

