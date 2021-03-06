package com.ninty.runtime.heap;

import com.ninty.classfile.ClassFile;
import com.ninty.classpath.ClassPath;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.heap.constantpool.NiConstant;
import com.ninty.runtime.heap.constantpool.NiConstantPool;
import com.ninty.utils.VMUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ninty on 2017/7/24.
 */
public class NiClassLoader {
    private ClassPath clzp;
    private Map<String, NiClass> classes;

    private final static String J_CLASS = "java/lang/Class";

    public NiClassLoader(ClassPath clzp) {
        this.clzp = clzp;
        classes = new HashMap<>(1 << 10);
        loadBasicClasses();
        loadPrimitiveClasses();
    }

    private void loadBasicClasses() {
        loadClass(J_CLASS);
        for (String className : classes.keySet()) {
            NiClass clz = classes.get(className);
            fillJClass(clz);
        }
    }

    private void loadPrimitiveClasses() {
        for (String classname : VMUtils.primitiveTypes.keySet()) {
            NiClass clz = new NiClass();
            clz.className = classname;
            clz.loader = this;
            clz.accessFlags = ClassConstant.ACC_PUBLIC;
            fillJClass(clz);
            classes.put(classname, clz);
        }
    }

    private void fillJClass(NiClass clz) {
        NiClass jClass = loadClass(J_CLASS);
        if (clz.getjClass() == null) {
            NiObject jObj = jClass.newObject();
            jObj.setExtra(clz);
            clz.setjClass(jObj);
        }
    }

    public NiClass getClass(String className) {
        return classes.get(className);
    }

    public NiClass loadClass(byte[] datas) {
        NiClass clz = definedClass(datas);
        link(clz);
        fillJClass(clz);
        return clz;
    }

    public NiClass loadClass(String className) {
        className = VMUtils.normalizeClassName(className);
        if (classes.containsKey(className)) {
            return classes.get(className);
        }
        NiClass clz;
        if (className.charAt(0) == '[') {
            clz = loadArrayClass(className);
        } else {
            clz = loadNonArrayClass(className);
        }
        hackClassloader(clz);
        if (classes.containsKey(J_CLASS)) {
            fillJClass(clz);
        }
        classes.put(className, clz);
        return clz;
    }

    private void hackClassloader(NiClass clz) {
        if (clz.className.equals("java/lang/ClassLoader")) {
            NiMethod loadLibrary = clz.getMethodByName("loadLibrary");
            loadLibrary.setCodes(new CodeBytes(new byte[]{(byte) 0xb1}));
        }
    }

    private NiClass loadArrayClass(String className) {
        NiClass clz = new NiClass(ClassConstant.ACC_PUBLIC, className, "java/lang/Object", new
                String[]{"java/lang/Cloneable", "java/io/Serializable"});
        resolve(clz);
        return clz;
    }

    private NiClass loadNonArrayClass(String className) {
        byte[] datas = readClass(className);
        NiClass clz = definedClass(datas);
        link(clz);
        return clz;
    }

    private byte[] readClass(String className) {
        byte[] datas = clzp.readClass(className);
        if (datas == null || datas.length == 0) {
            throw new RuntimeException("ClassNotFound : " + className);
        }
        return datas;
    }

    private NiClass definedClass(byte[] datas) {
        ClassFile cf = new ClassFile(datas);
        NiClass clz = new NiClass(cf);
        resolve(clz);
        return clz;
    }

    private void resolve(NiClass clz) {
        clz.loader = this;
        resovleSuperClass(clz);
        resolveInterfaces(clz);
    }

    private void resovleSuperClass(NiClass clz) {
        if (!clz.className.equals("java/lang/Object")) {
            clz.superClass = loadClass(clz.superClassName);
        }
    }

    private void resolveInterfaces(NiClass clz) {
        clz.interfaces = new NiClass[clz.interfaceNames.length];
        for (int i = 0; i < clz.interfaceNames.length; i++) {
            clz.interfaces[i] = loadClass(clz.interfaceNames[i]);
        }
    }

    private void link(NiClass clz) {
        varify(clz);
        prepare(clz);
    }

    private void varify(NiClass clz) {
        // TODO I will come back in one day
    }

    private void prepare(NiClass clz) {
        calcInstanceFieldSlotIds(clz);
        calcStaticFieldSlotIds(clz);
        allocAndInitStaticVars(clz);
    }

    private void calcInstanceFieldSlotIds(NiClass clz) {
        int slotId = 0;
        if (clz.superClass != null) {
            slotId = clz.superClass.instantceSlotCount;
        }
        for (NiField field : clz.getFields()) {
            if (!field.isStatic()) {
                field.slotId = slotId;
                slotId++;
                if (field.isLongOrDouble()) {
                    slotId++;
                }
            }
        }
        clz.instantceSlotCount = slotId;
    }

    private void calcStaticFieldSlotIds(NiClass clz) {
        int slotId = 0;
        for (NiField field : clz.getFields()) {
            if (field.isStatic()) {
                field.slotId = slotId;
                slotId++;
                if (field.isLongOrDouble()) {
                    slotId++;
                }
            }
        }
        clz.staticSlotCount = slotId;
    }

    private void allocAndInitStaticVars(NiClass clz) {
        clz.staticSlots = new LocalVars(clz.staticSlotCount);
        for (NiField field : clz.getFields()) {
            if (field.isStatic() && field.isFinal()) {
                initStaticFinalVar(clz, field);
            }
        }
    }

    private void initStaticFinalVar(NiClass clz, NiField field) {
        LocalVars slots = clz.staticSlots;
        NiConstantPool cps = clz.getCps();
        int cpIndex = field.constantValueIndex;
        int slotId = field.slotId;
        if (cpIndex > 0) {
            NiConstant cs = cps.get(cpIndex);
            switch (field.desc) {
                case "Z":
                case "B":
                case "C":
                case "S":
                case "I":
                    slots.setInt(slotId, ((NiConstant.NiInteger) cs).value);
                    break;
                case "J":
                    slots.setLong(slotId, ((NiConstant.NiLong) cs).value);
                    break;
                case "F":
                    slots.setFloat(slotId, ((NiConstant.NiFloat) cs).value);
                    break;
                case "D":
                    slots.setDouble(slotId, ((NiConstant.NiDouble) cs).value);
                    break;
                case "Ljava/lang/String;":
                    NiClassLoader loader = clz.getLoader();
                    String value = ((NiConstant.NiStr) cs).value;
                    slots.setRef(slotId, NiString.newString(loader, value));
                    break;
            }
        }

    }
}
