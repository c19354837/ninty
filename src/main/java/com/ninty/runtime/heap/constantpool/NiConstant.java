package com.ninty.runtime.heap.constantpool;

import com.ninty.classfile.constantpool.ConstantInfo;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiConstant {
    NiConstantPool cp;

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
                nicp = new NiStr((ConstantInfo.CPString) cp);
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
            case cp_method_type:
                nicp  = new NiMethodType((ConstantInfo.CPMethodType) cp);
                break;
            case cp_method_handle:
                nicp = new NiMethodHandleInfo((ConstantInfo.CPMethodHandleInfo) cp);
                break;
            case cp_invoke_dynamic:
                nicp = new NiInvokeDynamic((ConstantInfo.CPInvokeDynamic) cp);
                break;
            default:
                throw new ClassFormatException("unsupported constant poll type:" + cp.getClass().getSimpleName());
        }
        return nicp;
    }

    public static class NiInteger extends NiConstant {
        public int value;

        public NiInteger(ConstantInfo.CPInteger cp) {
            value = cp.value();
        }
    }

    public static class NiFloat extends NiConstant {
        public float value;

        NiFloat(ConstantInfo.CPFloat cp) {
            value = cp.value();
        }
    }

    public static class NiLong extends NiConstant {
        public long value;

        NiLong(ConstantInfo.CPLong cp) {
            value = cp.value();
        }
    }

    public static class NiDouble extends NiConstant {
        public double value;

        NiDouble(ConstantInfo.CPDouble cp) {
            value = cp.value();
        }
    }

    public static class NiClassInfo extends NiConstant {
        public String className;

        NiClassInfo(ConstantInfo.CPClass cp) {
            className = cp.className();
        }
    }

    public static class NiStr extends NiConstant {
        public String value;

        NiStr(ConstantInfo.CPString cp) {
            value = cp.string();
        }
    }

    public static class NiUtf8 extends NiConstant {
        public String value;

        public NiUtf8(ConstantInfo.CPUtf8 cp) {
            value = cp.value();
        }
    }

    public static class NiInvokeDynamic extends NiConstant {
        public NiInvokeDynamic(ConstantInfo.CPInvokeDynamic cp) {
            // TODO
        }
    }

    public static class NiMethodType extends NiConstant {
        public NiMethodType(ConstantInfo.CPMethodType cp) {
            // TODO
        }
    }

    public static class NiMethodHandleInfo extends NiConstant {
        public NiMethodHandleInfo(ConstantInfo.CPMethodHandleInfo cp) {
            // TODO
        }
    }
}
