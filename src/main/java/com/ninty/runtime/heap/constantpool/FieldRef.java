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
                throw new NoSuchFieldError(this.toString());
            }
            if (!field.canAccess(cp.clz)) {
                throw new IllegalAccessError(cp.clz + " cannot access " + field);
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
            NiField field = lookUpFields(c, name, desc);
            if (field != null) {
                return field;
            }
        }

        if (clz.getSuperClass() != null) {
            NiField field = lookUpFields(clz.getSuperClass(), name, desc);
            if (field != null) {
                return field;
            }
        }

        return null;
    }

    public NiField getField() {
        return field;
    }

    @Override
    public String toString() {
        return "FieldRef{" +
                "className='" + className + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
