package com.ninty.runtime.heap;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.ninty.classfile.constantpool.ConstantPoolInfos;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiConstantPool {
    NiClass clz;
    NiConstant[] constants;

    public NiConstantPool(NiClass clz, ConstantPoolInfos infos) {
        this.clz = clz;
        ConstantInfo[] cps = infos.getCps();
        constants = new NiConstant[cps.length];
        for (int i = 1; i < cps.length; i++) {
            if (cps[i] instanceof ConstantInfo.CPNameAndType) {
                continue;
            }
            constants[i] = NiConstant.generate(cps[i]);
            if(cps[i] instanceof ConstantInfo.CPLong || cps[i] instanceof ConstantInfo.CPDouble){
                i++;
            }
        }
    }
}
