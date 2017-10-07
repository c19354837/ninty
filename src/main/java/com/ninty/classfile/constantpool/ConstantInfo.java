package com.ninty.classfile.constantpool;

import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by ninty on 2017/7/9.
 */
public class ConstantInfo {

    public CPType type;
    public ConstantPoolInfos cps;

    static ConstantInfo generate(ConstantPoolInfos cps, ByteBuffer bb) {
        CPType type = CPType.get(bb.get());
        ConstantInfo cp;
        switch (type) {
            case cp_integer:
                cp = new CPInteger(bb);
                break;
            case cp_long:
                cp = new CPLong(bb);
                break;
            case cp_float:
                cp = new CPFloat(bb);
                break;
            case cp_double:
                cp = new CPDouble(bb);
                break;
            case cp_string:
                cp = new CPString(bb);
                break;
            case cp_utf8:
                cp = new CPUtf8(bb);
                break;
            case cp_class:
                cp = new CPClass(bb);
                break;
            case cp_name_type:
                cp = new CPNameAndType(bb);
                break;
            case cp_field:
            case cp_method:
            case cp_interface_method:
                cp = new CPMemeber(bb);
                break;
            case cp_method_type:
                cp = new CPMethodType(bb);
                break;
            case cp_method_handle:
                cp = new CPMethodHandleInfo(bb);
                break;
            case cp_invoke_dynamic:
                cp = new CPInvokeDynamic(bb);
                break;
            default:
                throw new ClassFormatException("unsupported constant poll type: " + type);
        }
        cp.type = type;
        cp.cps = cps;
        return cp;
    }

    public static class CPInteger extends ConstantInfo {
        private int value;

        CPInteger(ByteBuffer bb) {
            value = bb.getInt();
        }

        public int value() {
            return value;
        }
    }

    public static class CPFloat extends ConstantInfo {
        private float value;

        CPFloat(ByteBuffer bb) {
            value = bb.getFloat();
        }

        public float value() {
            return value;
        }
    }

    public static class CPLong extends ConstantInfo {
        private long value;

        CPLong(ByteBuffer bb) {
            value = bb.getLong();
        }

        public long value() {
            return value;
        }
    }

    public static class CPDouble extends ConstantInfo {
        private double value;

        CPDouble(ByteBuffer bb) {
            value = bb.getDouble();
        }

        public double value() {
            return value;
        }
    }

    public static class CPClass extends ConstantInfo {
        private int classIndex;

        CPClass(ByteBuffer bb) {
            classIndex = bb.getChar();
        }

        public String className() {
            return cps.getUtf8(classIndex);
        }
    }

    public static class CPNameAndType extends ConstantInfo {
        private int nameIndex;
        private int descIndex;

        CPNameAndType(ByteBuffer bb) {
            nameIndex = bb.getChar();
            descIndex = bb.getChar();
        }

        public String name() {
            return cps.getUtf8(nameIndex);
        }

        public String desc() {
            return cps.getUtf8(descIndex);
        }
    }

    public static class CPString extends ConstantInfo {
        private int stringIndex;

        CPString(ByteBuffer bb) {
            stringIndex = bb.getChar();
        }

        public String string() {
            return cps.getUtf8(stringIndex);
        }
    }

    public static class CPMemeber extends ConstantInfo {
        private int classIndex;
        private int nameAndTypeIndex;

        CPMemeber(ByteBuffer bb) {
            classIndex = bb.getChar();
            nameAndTypeIndex = bb.getChar();
        }

        public String className() {
            return ((CPClass) cps.get(classIndex)).className();
        }

        public String name() {
            return ((CPNameAndType) cps.get(nameAndTypeIndex)).name();
        }

        public String desc() {
            return ((CPNameAndType) cps.get(nameAndTypeIndex)).desc();
        }
    }

    public static class CPUtf8 extends ConstantInfo {
        private String value;

        CPUtf8(ByteBuffer bb) {
            int len = bb.getChar();
            byte[] datas = new byte[len];
            bb.get(datas);
            try {
                value = decodeMutf8(datas);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ClassFormatException("cannot read the utf8");
            }
        }

        public String value() {
            return value;
        }

        private String decodeMutf8(byte[] bytes) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeShort(bytes.length);
            dos.write(bytes);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            DataInputStream dis = new DataInputStream(bais);
            return dis.readUTF();
        }
    }

    public static class CPInvokeDynamic extends ConstantInfo {
        private int bootstrapMethodAttrIndex;
        private int nameAndTypeIndex;

        CPInvokeDynamic(ByteBuffer bb) {
            bootstrapMethodAttrIndex = bb.getChar();
            nameAndTypeIndex = bb.getChar();
        }

        public int bootIndex() {
            return bootstrapMethodAttrIndex;
        }

        public String name() {
            return ((CPNameAndType) cps.get(nameAndTypeIndex)).name();
        }

        public String desc() {
            return ((CPNameAndType) cps.get(nameAndTypeIndex)).desc();
        }
    }

    public static class CPMethodHandleInfo extends ConstantInfo {
        private int referenceKind;
        private int referenceIndex;

        public CPMethodHandleInfo(ByteBuffer bb) {
            referenceKind = bb.get();
            referenceIndex = bb.getChar();
        }

        public int getReference() {
            return referenceIndex;
        }
    }

    public static class CPMethodType extends ConstantInfo {
        private int descriptorIndex;

        public CPMethodType(ByteBuffer bb) {
            descriptorIndex = bb.getChar();
        }
    }


    public enum CPType {
        cp_utf8(1), cp_integer(3), cp_float(4), cp_long(5), cp_double(6), cp_class(7), cp_string(8), cp_field(9),
        cp_method(10), cp_interface_method(11), cp_name_type(12), cp_method_handle(15), cp_method_type(16),
        cp_invoke_dynamic(18);

        int tag;

        CPType(int tag) {
            this.tag = tag;
        }

        static CPType get(int tag) {
            for (CPType type : values()) {
                if (type.tag == tag) {
                    return type;
                }
            }
            throw new IllegalArgumentException("illegal constant pool tag:" + tag);
        }
    }
}
