package com.ninty.runtime.heap.constantpool;

import com.ninty.runtime.heap.NiClass;

/**
 * Created by ninty on 2017/7/24.
 */
public abstract class BaseSymbol extends NiConstant {
    NiConstantPool cp;
    String className;
    NiClass clz;

    protected void resolveClass() {
        if (clz == null) {
            NiClass c = cp.clz.getLoader().loadClass(className);
            if (!cp.clz.canAccess(c)) {
                throw new RuntimeException(cp.clz.getClassName() + " can not access to " + className);
            }
            clz = c;
        }
    }

    abstract protected void resolve();
}
