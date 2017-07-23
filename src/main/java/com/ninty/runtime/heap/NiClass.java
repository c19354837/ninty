package com.ninty.runtime.heap;

import com.ninty.classfile.ClassFile;
import com.ninty.classfile.MemberInfo;

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
    NiFiled[] fileds;
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
        MemberInfo[] filedInfos = classFile.getFiledInfos();
        fileds = new NiFiled[filedInfos.length];
        for (int i = 0; i < filedInfos.length; i++) {
            fileds[i] = new NiFiled(this, filedInfos[i]);
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
