package com.ninty.runtime.heap.constantpool;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.ninty.classfile.constantpool.ConstantPoolInfos;
import com.ninty.runtime.heap.NiClass;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiConstantPool {
    NiClass clz;
    private NiConstant[] constants;

    public NiConstantPool(NiClass clz, ConstantPoolInfos infos) {
        this.clz = clz;
        ConstantInfo[] cps = infos.getCps();
        constants = new NiConstant[cps.length];
        for (int i = 1; i < cps.length; i++) {
            if (cps[i] instanceof ConstantInfo.CPNameAndType) {
                continue;
            }
            constants[i] = NiConstant.generate(cps[i]);
            constants[i].cp = this;
            if (cps[i] instanceof ConstantInfo.CPLong || cps[i] instanceof ConstantInfo.CPDouble) {
                i++;
            }
        }
    }

    public NiConstant get(int index) {
        return constants[index];
    }
}
