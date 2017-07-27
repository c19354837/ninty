package com.ninty.runtime.heap.constantpool;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiField;

/**
 * Created by ninty on 2017/7/24.
 */
public class FieldRef extends MemberRef {
    private NiField field;

    public FieldRef(ConstantInfo.CPMemeber cp) {
        super(cp);
    }

    @Override
    public void resolve() {
        if (this.field == null) {
            resolveClass();
            NiField field = lookUpFields(clz, name, desc);
            if (field == null) {
                throw new RuntimeException("NoSuchField clz:" + clz.getClassName() + ", name:" + name + ", desc:" + desc);
            }
            if (!field.canAccess(cp.clz)) {
                throw new RuntimeException(cp.clz.getClassName() + " can not access to " + field);
            }
            this.field = field;
        }
    }

    private NiField lookUpFields(NiClass clz, String name, String desc) {
        for (NiField field : clz.getFields()) {
            if (field.getName().equals(name) && field.getDesc().equals(desc)) {
                return field;
            }
        }

        for (NiClass c : clz.getInterfaces()) {
            return lookUpFields(c, name, desc);
        }

        if (clz.getSuperClass() != null) {
            return lookUpFields(clz.getSuperClass(), name, desc);
        }

        return null;
    }

    public NiField getField() {
        return field;
    }
}
