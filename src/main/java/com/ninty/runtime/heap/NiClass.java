package com.ninty.runtime.heap;

import com.ninty.classfile.ClassFile;
import com.ninty.classfile.MemberInfo;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.heap.constantpool.NiConstantPool;

import java.util.HashMap;
import java.util.Map;

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

    private Map<String, String> primitiveTypes = new HashMap<>(16);

    {
        primitiveTypes.put("void", "V");
        primitiveTypes.put("boolean", "Z");
        primitiveTypes.put("byte", "B");
        primitiveTypes.put("short", "S");
        primitiveTypes.put("char", "C");
        primitiveTypes.put("int", "I");
        primitiveTypes.put("long", "L");
        primitiveTypes.put("float", "F");
        primitiveTypes.put("double", "D");
    }

    public NiClass(ClassFile classFile) {
        accessFlags = classFile.getAccessFlags();
        className = classFile.getClassName();
        superClassName = classFile.getSuperClassName();
        interfaceNames = classFile.getInterfaceNames();

        initCP(classFile);
        initFiled(classFile);
        initMethod(classFile);
    }

    public NiClass(int accessFlags, String className, String superClassName, String[] interfaceNames) {
        this.accessFlags = accessFlags;
        this.className = className;
        this.superClassName = superClassName;
        this.interfaceNames = interfaceNames;
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
        if(s.isArray()){
            if(t.isArray()){
                NiClass sc = s.componentClass();
                NiClass tc = t.componentClass();
                return sc == tc || tc.isAssignableFrom(sc);
            }else{
                if (t.isInterface()) {
                    return t.className.equals("java/lang/Cloneable") || t.className.equals("java/io/Serializable");
                } else {
                    return t.className.equals("java/lang/Object");
                }
            }
        }else{
            if(s.isInterface()){
                if(t.isInterface()){
                    return s.isSubClass(t);
                }else {
                    return t.className.equals("java/lang/Object");
                }
            }else{
                if(t.isInterface()){
                    return s.isImplements(t);
                }else {
                    return s.isSubClass(t);
                }
            }
        }
    }

    public boolean isSame(NiClass clz) {
        if (clz == null) {
            return false;
        }
        return clz.className.equals(className) && clz.loader == loader;
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

    public NiObject newArray(int count) {
        if (!isArray()) {
            throw new IllegalAccessError("Current class is not an array:" + this);
        }
        switch (className.substring(0, 2)) {
            case "[Z":
                return new NiObject(this, new byte[count]);
            case "[B":
                return new NiObject(this, new byte[count]);
            case "[C":
                return new NiObject(this, new char[count]);
            case "[S":
                return new NiObject(this, new short[count]);
            case "[I":
                return new NiObject(this, new int[count]);
            case "[J":
                return new NiObject(this, new long[count]);
            case "[F":
                return new NiObject(this, new float[count]);
            case "[D":
                return new NiObject(this, new double[count]);
            default:
                return new NiObject(this, new NiObject[count]);
        }
    }

    public boolean isArray() {
        return className.charAt(0) == '[';
    }

    /**
     * convert classname to Array class
     */
    public void toArrayClass() {
        if (!isArray()) {
            String type = primitiveTypes.get(className);
            if (type == null) {
                className = "L" + className + ";";
            } else {
                className = type;
            }
        }
        className = "[" + className;
    }

    public NiClass componentClass() {
        if (isArray()) {
            return loader.loadClass(toClassname(className.substring(1)));
        }
        throw new IllegalAccessError("Current class is not an array:" + this);
    }

    private String toClassname(String desc) {
        if (desc.charAt(0) == '[') {
            return desc;
        }
        if (desc.charAt(0) == 'L') {
            return desc.substring(0, desc.length() - 1);
        }
        for (String key : primitiveTypes.keySet()) {
            if (primitiveTypes.get(key).equals(desc)) {
                return key;
            }
        }
        throw new IllegalArgumentException("invalid descriptor:" + desc);
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
