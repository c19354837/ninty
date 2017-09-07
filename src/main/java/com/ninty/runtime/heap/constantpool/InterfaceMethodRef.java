package com.ninty.runtime.heap.constantpool;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiMethod;

/**
 * Created by ninty on 2017/7/24.
 */
public class InterfaceMethodRef extends MemberRef {
    private NiMethod method;

    InterfaceMethodRef(ConstantInfo.CPMemeber cp) {
        super(cp);
    }

    @Override
    public void resolve() {
        if (method == null) {
            resolveClass();
            if (!clz.isInterface()) {
                throw new IncompatibleClassChangeError("expect for interface while it's class: " + clz);
            }
            NiMethod method = lookUpMethods(clz, name, desc);
            if (method == null) {
                throw new NoSuchMethodError(this.toString());
            }
            if (!method.canAccess(cp.clz)) {
                throw new IllegalAccessError(cp.clz + " cannot access " + method);
            }
            this.method = method;
        }
    }

    private NiMethod lookUpMethods(NiClass clz, String name, String desc) {
        for (NiMethod method : clz.getMethods()) {
            if (method.getName().equals(name) && method.getDesc().equals(desc)) {
                return method;
            }
        }

        for (NiClass c : clz.getInterfaces()) {
            NiMethod method = lookUpMethods(c, name, desc);
            if (method != null) {
                return method;
            }
        }

        return null;
    }

    public NiMethod getMethod() {
        return method;
    }
}
