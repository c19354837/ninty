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

    public static ConstantInfo generate(ConstantPoolInfos cps, ByteBuffer bb){
        CPType type = CPType.get(bb.get());
        ConstantInfo cp = null;
        switch (type){
            case cp_utf8:
                cp = new CPUtf8(bb);
                break;
            case cp_integer:
                cp = new CPInteger(bb);
                break;
            case cp_long:
                cp = new CPFLong(bb);
                break;
            case cp_float:
                cp = new CPFloat(bb);
                break;
            case cp_double:
                cp = new CPDouble(bb);
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
            default:
                throw new ClassFormatException("unsupported constant poll type:" + type);
        }
        cp.type = type;
        cp.cps = cps;
        return cp;
    }

    static class CPInteger extends ConstantInfo {
        int value;
        CPInteger(ByteBuffer bb){
            value = bb.getInt();
        }
    }

    static class CPFloat extends ConstantInfo {
        float value;
        CPFloat(ByteBuffer bb){
            value = bb.getFloat();
        }
    }

    static class CPFLong extends ConstantInfo {
        long value;
        CPFLong(ByteBuffer bb){
            value = bb.getLong();
        }
    }

    static class CPDouble extends ConstantInfo {
        double value;
        CPDouble(ByteBuffer bb){
            value = bb.getDouble();
        }
    }

    static class CPClass extends ConstantInfo{
        private int classIndex;

        CPClass(ByteBuffer bb){
            classIndex = bb.getChar();
        }

        public String className(){
            return cps.getUtf8(classIndex);
        }
    }

    static class CPNameAndType extends ConstantInfo{
        private int nameIndex;
        private int descIndex;

        public CPNameAndType(ByteBuffer bb){
            nameIndex = bb.getChar();
            descIndex = bb.getChar();
        }

        public String name(){
            return cps.getUtf8(nameIndex);
        }

        public String desc(){
            return cps.getUtf8(descIndex);
        }
    }

    static class CPString extends ConstantInfo {
        private int stringIndex;

        CPString(ByteBuffer bb){
            stringIndex = bb.getChar();
        }

        public String string(){
            return cps.getUtf8(stringIndex);
        }
    }

    static class CPMemeber extends ConstantInfo {
        private int classIndex;
        private int nameAndTypeIndex;

        CPMemeber(ByteBuffer bb){
            classIndex = bb.getChar();
            nameAndTypeIndex = bb.getChar();
        }

        public String className(){
            return ((CPClass)cps.get(classIndex)).className();
        }

        public String name(){
            return ((CPNameAndType)cps.get(nameAndTypeIndex)).name();
        }

        public String desc(){
            return ((CPNameAndType)cps.get(nameAndTypeIndex)).desc();
        }
    }

    static class CPUtf8 extends ConstantInfo {
        String value;

        CPUtf8(ByteBuffer bb){
            int len = bb.getChar();
            byte[] datas = new byte[len];
            try {
                value = decodeMutf8(datas);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ClassFormatException("cannot read the utf8");
            }
        }

        private String decodeMutf8(byte[] bytes) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length + 2);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeShort(bytes.length);
            dos.write(bytes);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            DataInputStream dis = new DataInputStream(bais);
            return dis.readUTF();
        }
    }

    enum CPType{
        cp_utf8(1), cp_integer(3), cp_float(4), cp_long(5), cp_double(6), cp_class(7), cp_string(8), cp_field(9), cp_method(10),
        cp_interface_method(11), cp_name_type(12), cp_method_handle(15), cp_method_type(16), cp_invoke_dynamic(18);

        int tag;

        CPType(int tag){
            this.tag = tag;
        }

        static CPType get(int tag){
            for(CPType type : values()){
                if(type.tag == tag){
                    return type;
                }
            }
            throw new IllegalArgumentException("illegal constant pool tag:" + tag);
        }
    }
}
