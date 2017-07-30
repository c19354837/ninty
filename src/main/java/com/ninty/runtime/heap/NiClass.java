package com.ninty.runtime.heap;

import com.ninty.classfile.ClassFile;
import com.ninty.classfile.MemberInfo;
import com.ninty.runtime.LocalVars;
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
    private NiConstantPool cps;
    private NiField[] fields;
    private NiMethod[] methods;

    NiClassLoader loader;
    LocalVars staticSlots;
    int instantceSlotCount;
    int staticSlotCount;

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

    public NiMethod getMainMethod() {
        for (NiMethod method : methods) {
            if (method.getName().equals("main") && method.getDesc().equals("([Ljava/lang/String;)V")) {
                return method;
            }
        }
        return null;
    }

    public boolean isAssignableFrom(NiClass clz) {
        NiClass s = clz;
        NiClass t = this;
        if (s == t) {
            return true;
        }
        if (t.isInterface()) {
            return s.isImplements(t);
        } else {
            return s.isSubClass(t);
        }
    }

    public boolean isSubOf(NiClass clz) {
        NiClass c = this.superClass;
        while (c != null) {
            if (c == clz) {
                return true;
            }
            c = c.superClass;
        }
        return false;
    }

    /**
     * this implements clz
     */
    public boolean isImplements(NiClass clz) {
        for (NiClass c = this; c != null; c = c.superClass) {
            if (c == clz || c.isSubInterfaceOf(clz)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSubInterfaceOf(NiClass c) {
        for (NiClass interf : this.interfaces) {
            if (interf == c || interf.isSubInterfaceOf(c)) {
                return true;
            }
        }
        return false;
    }

    public NiObject newObject() {
        return new NiObject(this, instantceSlotCount);
    }

    public boolean isPublic() {
        return (accessFlags & ClassConstant.ACC_PUBLIC) != 0;
    }

    /**
     * clz extend this
     */
    public boolean isSubClass(NiClass clz) {
        return (clz.superClassName != null && clz.superClassName.equals(superClassName)) || this.isSubClass(clz.getSuperClass());
    }

    /**
     * clz extend this
     */
    public boolean isSuperClass(NiClass clz) {
        return !isSubClass(clz);
    }

    public boolean isAbstract() {
        return (accessFlags & ClassConstant.ACC_ABSTRACT) != 0;
    }

    public boolean isInterface() {
        return (accessFlags & ClassConstant.ACC_INTERFACE) != 0;
    }

    public String packageName() {
        int indexOf = className.lastIndexOf('/');
        return indexOf > -1 ? className.substring(0, indexOf) : "";
    }

    public boolean isSamePackge(NiClass clz) {
        return packageName().equals(clz.packageName());
    }

    public boolean canAccess(NiClass clz) {
        return clz.isPublic() || clz.packageName().equals(this.packageName());
    }

    public String getClassName() {
        return className;
    }

    public NiClass getSuperClass() {
        return superClass;
    }

    public NiClass[] getInterfaces() {
        return interfaces;
    }

    public NiField[] getFields() {
        return fields;
    }

    public NiMethod[] getMethods() {
        return methods;
    }

    public NiClassLoader getLoader() {
        return loader;
    }

    public NiConstantPool getCps() {
        return cps;
    }

    public LocalVars getStaticSlots() {
        return staticSlots;
    }

    @Override
    public String toString() {
        return className;
    }
}
