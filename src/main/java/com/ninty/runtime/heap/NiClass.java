package com.ninty.runtime.heap;

import com.ninty.classfile.AnnotationAttr;
import com.ninty.classfile.AttributeInfo;
import com.ninty.classfile.ClassFile;
import com.ninty.classfile.MemberInfo;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.NiFrame;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.heap.constantpool.NiConstantPool;
import com.ninty.utils.VMUtils;

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

    private AttributeInfo[] attributeInfos;
    private byte[] annotationDatas;

    private String sourceFile;

    NiClassLoader loader;
    LocalVars staticSlots;
    int instantceSlotCount;
    int staticSlotCount;

    private boolean clinit;
    private NiObject jClass; // class's class, className=java/lang/Class

    public NiClass() {
    }

    public NiClass(ClassFile classFile) {
        accessFlags = classFile.getAccessFlags();
        className = classFile.getClassName();
        superClassName = classFile.getSuperClassName();
        interfaceNames = classFile.getInterfaceNames();

        initCP(classFile);
        initFiled(classFile);
        initMethod(classFile);
        initSourceFile(classFile);
        initAnnotation(classFile);

    }

    public NiClass(int accessFlags, String className, String superClassName, String[] interfaceNames) {
        this.accessFlags = accessFlags;
        this.className = className;
        this.superClassName = superClassName;
        this.interfaceNames = interfaceNames;
        methods = new NiMethod[0];
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

    private void initSourceFile(ClassFile classFile) {
        AttributeInfo[] attributeInfos = classFile.getAttributeInfos();
        for (int i = 0; i < attributeInfos.length; i++) {
            if (attributeInfos[i] instanceof AttributeInfo.AttrSourceFile) {
                sourceFile = ((AttributeInfo.AttrSourceFile) attributeInfos[i]).sourceFile;
                return;
            }
        }
        sourceFile = "unknow";
    }

    private void initAnnotation(ClassFile classFile) {
        attributeInfos = classFile.getAttributeInfos();
        for (int i = 0; i < attributeInfos.length; i++) {
            if (attributeInfos[i] instanceof AnnotationAttr.RuntimeVisibleAnnotations) {
                annotationDatas = ((AnnotationAttr.RuntimeVisibleAnnotations) attributeInfos[i]).annotationDatas;
            }
        }
    }

    public NiMethod getMainMethod() {
        return getMethod("main", "([Ljava/lang/String;)V");
    }

    public NiMethod getClinitMethod() {
        return getMethod("<clinit>", "()V");
    }


    public NiMethod getDefaultInitMethod() {
        return getInitMethod("()V");
    }

    public NiMethod getInitMethod(String desc) {
        return getMethod("<init>", desc);
    }

    public NiMethod getToStringMethod() {
        return getMethod("toString", "()Ljava/lang/String;");
    }

    public NiMethod getMethod(String name, String desc) {
        for (NiMethod method : methods) {
            if (method.getName().equals(name) && method.getDesc().equals(desc)) {
                return method;
            }
        }
        return null;
    }

    public NiField findField(String name, String desc) {
        for (NiField field : fields) {
            if (field.getName().equals(name) && field.getDesc().equals(desc)) {
                return field;
            }
        }
        if(this.superClass != null){
            return this.superClass.findField(name, desc);
        }
        return null;
    }

    public boolean isAssignableFrom(NiClass clz) {
        NiClass s = clz;
        NiClass t = this;
        if (s == t) {
            return true;
        }
        if (s.isArray()) {
            if (t.isArray()) {
                NiClass sc = s.componentClass();
                NiClass tc = t.componentClass();
                return sc == tc || tc.isAssignableFrom(sc);
            } else {
                if (t.isInterface()) {
                    return t.className.equals("java/lang/Cloneable") || t.className.equals("java/io/Serializable");
                } else {
                    return t.className.equals("java/lang/Object");
                }
            }
        } else {
            if (s.isInterface()) {
                if (t.isInterface()) {
                    return s.isSubClass(t);
                } else {
                    return t.className.equals("java/lang/Object");
                }
            } else {
                if (t.isInterface()) {
                    return s.isImplements(t); // s implements t
                } else {
                    return t.isSubClass(s); // s extends t
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

    public boolean clinit(NiThread thread) {
        clinit = true;
        boolean result1 = scheduleClinit(thread);
        boolean result2 = initSuperClass(thread);
        return result1 || result2;
    }

    private boolean scheduleClinit(NiThread thread) {
        NiMethod clinitMethod = getClinitMethod();
        if (clinitMethod != null) {
            thread.pushFrame(new NiFrame(clinitMethod));
            return true;
        }
        return false;
    }

    private boolean initSuperClass(NiThread thread) {
        if (!isInterface() && superClass != null && !superClass.isClinit()) {
            return superClass.clinit(thread);
        }
        return false;
    }

    public NiObject newArray(int count) {
        if (!isArray()) {
            throw new IllegalAccessError("Current class is not an array:" + this);
        }
        switch (className.substring(0, 2)) {
            case "[Z":
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

    public boolean isPrimitive() {
        return VMUtils.primitiveTypes.containsKey(className);
    }

    /**
     * convert classname to Array class
     */
    public NiClass getArrayClass() {
        String className = this.className;
        if (!isArray()) {
            String type = VMUtils.primitiveTypes.get(className);
            if (type == null) {
                className = "L" + className + ";";
            } else {
                className = type;
            }
        }
        className = "[" + className;
        return getLoader().loadClass(className);
    }

    public NiClass componentClass() {
        if (isArray()) {
            return loader.loadClass(VMUtils.toClassname(className.substring(1)));
        }
        throw new IllegalAccessError("Current class is not an array:" + this);
    }

    public String javaName() {
        return className.replace('/', '.');
    }

    public boolean isPublic() {
        return (accessFlags & ClassConstant.ACC_PUBLIC) != 0;
    }

    /**
     * clz extend this
     */
    public boolean isSubClass(NiClass clz) {
        return clz != null && (clz.superClassName != null && clz.superClassName.equals(className) || this.isSubClass
                (clz.getSuperClass()));
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

    private String packageName() {
        int indexOf = className.lastIndexOf('/');
        return indexOf > -1 ? className.substring(0, indexOf) : "";
    }

    public boolean isSamePackge(NiClass clz) {
        return packageName().equals(clz.packageName());
    }

    public boolean canAccess(NiClass clz) {
        return clz.isPublic() || clz.packageName().equals(this.packageName());
    }

    public AttributeInfo.BootstrapMethodInfo getBootstrapMethodInfo(int index) {
        AttributeInfo.AttrBootstrapMethods bootstrapMethods = null;
        for (int i = 0; i < attributeInfos.length; i++) {
            if (attributeInfos[i] instanceof AttributeInfo.AttrBootstrapMethods) {
                bootstrapMethods = (AttributeInfo.AttrBootstrapMethods) attributeInfos[i];
            }
        }
        if (bootstrapMethods == null) {
            throw new ClassFormatError("can not find BootstrapMethods in: " + index + ", className: " + className);
        }
        return bootstrapMethods.bootstarpMethods[index];
    }

    public int getStaticInt(String name) {
        NiField field = findField(name, "I");
        return staticSlots.getInt(field.getSlotId());
    }

    public NiObject getStaticRef(String name, String desc) {
        NiField field = findField(name, desc);
        return staticSlots.getRef(field.getSlotId());
    }

    public void setStaticRef(String name, String desc, NiObject ref) {
        NiField field = findField(name, desc);
        staticSlots.setRef(field.getSlotId(), ref);
    }

    public int getAccessFlags() {
        return accessFlags;
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

    public NiObject getjClass() {
        return jClass;
    }

    void setjClass(NiObject jClass) {
        this.jClass = jClass;
    }

    public boolean isClinit() {
        return clinit;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public byte[] getAnnotationDatas() {
        return annotationDatas;
    }

    @Override
    public String toString() {
        return className;
    }
}
