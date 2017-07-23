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
    ConstantPoolInfos cps;
    int accessFlags; //u2
    String className;
    String superClassName;
    String[] interfaceNames;
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
        className = cps.getClassName(bb.getChar());
        superClassName = cps.getSuperClassName(bb.getChar());
        fillInterfaceInfos(bb);
        fillFiledInfos(bb);
        fillMethodInfos(bb);
        fillAttributeInfos(bb);
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
        cps = new ConstantPoolInfos(bb);
    }

    private void fillInterfaceInfos(ByteBuffer bb) {
        int interfaceCount = bb.getChar();
        interfaceNames = new String[interfaceCount];
        for (int i = 0; i < interfaceCount; i++) {
            interfaceNames[i] = cps.getClassName(bb.getChar());
        }
    }

    private void fillFiledInfos(ByteBuffer bb) {
        int filedCount = bb.getChar();
        filedInfos = new MemberInfo[filedCount];
        for (int i = 0; i < filedCount; i++) {
            filedInfos[i] = new MemberInfo(cps, bb);
        }
    }

    private void fillMethodInfos(ByteBuffer bb) {
        int methodCount = bb.getChar();
        methodInfos = new MemberInfo[methodCount];
        for (int i = 0; i < methodCount; i++) {
            methodInfos[i] = new MemberInfo(cps, bb);
        }
    }

    private void fillAttributeInfos(ByteBuffer bb) {
        int attrCount = bb.getChar();
        attributeInfos = new AttributeInfo[attrCount];
        for (int i = 0; i < attrCount; i++) {
            attributeInfos[i] = AttributeInfo.generate(cps, bb);
        }
    }

    public ConstantPoolInfos getCps() {
        return cps;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public String getClassName() {
        return className;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public String[] getInterfaceNames() {
        return interfaceNames;
    }

    public MemberInfo[] getFiledInfos() {
        return filedInfos;
    }

    public MemberInfo[] getMethodInfos() {
        return methodInfos;
    }

    @Override
    public String toString() {
        return "ClassFile{" +
                "magic=" + magic +
                ", minorVersion=" + minorVersion +
                ", majorVersion=" + majorVersion +
                ", accessFlags=" + accessFlags +
                ", thisClass=" + className +
                ", superClass=" + superClassName +
                ", interfaceInfos=" + Arrays.toString(interfaceNames) +
                ", filedInfos=" + Arrays.toString(filedInfos) +
                ", methodInfos=" + Arrays.toString(methodInfos) +
                '}';
    }
}

