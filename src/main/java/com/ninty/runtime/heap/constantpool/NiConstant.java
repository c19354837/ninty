package com.ninty.runtime.heap.constantpool;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiConstant {

    public static NiConstant generate(ConstantInfo cp) {
        NiConstant nicp;
        switch (cp.type) {
            case cp_integer:
                nicp = new NiConstant.NiInteger((ConstantInfo.CPInteger) cp);
                break;
            case cp_long:
                nicp = new NiConstant.NiLong((ConstantInfo.CPLong) cp);
                break;
            case cp_float:
                nicp = new NiConstant.NiFloat((ConstantInfo.CPFloat) cp);
                break;
            case cp_double:
                nicp = new NiConstant.NiDouble((ConstantInfo.CPDouble) cp);
                break;
            case cp_string:
                nicp = new NiConstant.NiString((ConstantInfo.CPString) cp);
                break;
            case cp_utf8:
                nicp = new NiConstant.NiUtf8((ConstantInfo.CPUtf8) cp);
                break;
            case cp_class:
                nicp = new ClassRef((ConstantInfo.CPClass) cp);
                break;
            case cp_field:
                nicp = new FieldRef((ConstantInfo.CPMemeber) cp);
                break;
            case cp_method:
                nicp = new MethodRef((ConstantInfo.CPMemeber) cp);
                break;
            case cp_interface_method:
                nicp = new InterfaceMethodRef((ConstantInfo.CPMemeber) cp);
                break;
            default:
                throw new ClassFormatException("unsupported constant poll type:" + cp.getClass().getSimpleName());
        }
        return nicp;
    }

    static class NiInteger extends NiConstant {
        int value;

        public NiInteger(ConstantInfo.CPInteger cp) {
            value = cp.value();
        }
    }

    static class NiFloat extends NiConstant {
        float value;

        NiFloat(ConstantInfo.CPFloat cp) {
            value = cp.value();
        }
    }

    static class NiLong extends NiConstant {
        long value;

        NiLong(ConstantInfo.CPLong cp) {
            value = cp.value();
        }
    }

    static class NiDouble extends NiConstant {
        double value;

        NiDouble(ConstantInfo.CPDouble cp) {
            value = cp.value();
        }
    }

    static class NiClassInfo extends NiConstant {
        String className;

        NiClassInfo(ConstantInfo.CPClass cp) {
            className = cp.className();
        }
    }

    static class NiString extends NiConstant {
        String value;

        NiString(ConstantInfo.CPString cp) {
            value = cp.string();
        }
    }

    static class NiUtf8 extends NiConstant {
        String value;

        public NiUtf8(ConstantInfo.CPUtf8 cp) {
            value = cp.value();
        }
    }
}
