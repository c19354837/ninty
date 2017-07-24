package com.ninty.runtime.heap;

import com.ninty.classfile.ClassFile;
import com.ninty.classfile.MemberInfo;
import com.ninty.runtime.heap.constantpool.NiConstantPool;

/**
 * Created by ninty on 2017/7/23.
 */
public class NiClass {
    int accessFlags; //u2
    String className;
    String superClassName;
    String[] interfaceNames;

    NiClass superClass;
    NiClass[] interfaces;
    NiConstantPool cps;
    NiField[] fields;
    NiMethod[] methods;

    public NiClass(ClassFile classFile) {
        accessFlags = classFile.getAccessFlags();
        className = classFile.getClassName();
        superClassName = classFile.getSuperClassName();
        interfaceNames = classFile.getInterfaceNames();

        initCP(classFile);
        initFiled(classFile);
        initMethod(classFile);
    }

    private void initCP(ClassFile classFile) {
        cps = new NiConstantPool(this, classFile.getCps());
    }

    private void initFiled(ClassFile classFile) {
        MemberInfo[] fieldInfos = classFile.getFieldInfos();
        fields = new NiField[fieldInfos.length];
        for (int i = 0; i < fieldInfos.length; i++) {
            fields[i] = new NiField(this, fieldInfos[i]);
        }
    }

    private void initMethod(ClassFile classFile) {
        MemberInfo[] methodInfos = classFile.getMethodInfos();
        methods = new NiMethod[methodInfos.length];
        for (int i = 0; i < methodInfos.length; i++) {
            methods[i] = new NiMethod(this, methodInfos[i]);
        }
    }
}
