package com.ninty.classfile;

import com.ninty.classfile.constantpool.ConstantPoolInfos;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by ninty on 2017/7/9.
 */
public class ClassFile {
    int magic; //u4
    int minorVersion; //u2
    int majorVersion; //u2
    ConstantPoolInfos constantPoolInfos;
    int accessFlags; //u2
    int thisClass; // u2
    int superClass; //u2
    InterfaceInfo[] interfaceInfos;
    MemberInfo[] filedInfos;
    MemberInfo[] methodInfos;
    AttributeInfo[] attributeInfos;

    public ClassFile(byte[] datas) {
        resolve(datas);
    }

    private void resolve(byte[] datas) {
        ByteBuffer bb = ByteBuffer.wrap(datas);
        magic = bb.getInt();
        checkMagic();
        minorVersion = bb.getChar();
        majorVersion = bb.getChar();
        checkVersion();
        fillConstantPoll(bb);
        accessFlags = bb.getChar();
        thisClass = bb.getChar();
        superClass = bb.getChar();
        fillInterfaceInfos(bb);
        fillFiledInfos(bb);
        fillMethodInfos(bb);
        fillAttributeInfo(bb);
    }

    private void checkMagic() {
        if (magic != 0xCAFEBABE) {
            throw new ClassFormatException("shoud start with 0xCAFEBABE");
        }
    }

    private void checkVersion() {
        switch (majorVersion) {
            case 45:
                return;
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
                if (minorVersion == 0) {
                    return;
                }
        }
        throw new UnsupportedClassVersionError("majorVersion:" + majorVersion + ", minorVersion:" + minorVersion);
    }

    private void fillConstantPoll(ByteBuffer bb) {
        constantPoolInfos = new ConstantPoolInfos(bb);
    }

    private void fillInterfaceInfos(ByteBuffer bb) {
        int interfaceCount = bb.getChar();
        interfaceInfos = new InterfaceInfo[interfaceCount];
    }

    private void fillFiledInfos(ByteBuffer bb) {
        int filedCount = bb.getChar();
        filedInfos = new MemberInfo[filedCount];
    }

    private void fillMethodInfos(ByteBuffer bb) {
        int methodCount = bb.getChar();
        methodInfos = new MemberInfo[methodCount];
    }

    private void fillAttributeInfo(ByteBuffer bb) {
        int attributeCount = bb.getChar();
        attributeInfos = new AttributeInfo[attributeCount];
    }

    @Override
    public String toString() {
        return "ClassFile{" +
                "magic=" + magic +
                ", minorVersion=" + minorVersion +
                ", majorVersion=" + majorVersion +
                ", accessFlags=" + accessFlags +
                ", thisClass=" + thisClass +
                ", superClass=" + superClass +
                ", interfaceInfos=" + Arrays.toString(interfaceInfos) +
                ", filedInfos=" + Arrays.toString(filedInfos) +
                ", methodInfos=" + Arrays.toString(methodInfos) +
                ", attributeInfos=" + Arrays.toString(attributeInfos) +
                '}';
    }
}

