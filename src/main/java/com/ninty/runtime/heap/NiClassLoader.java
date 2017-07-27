package com.ninty.runtime.heap;

import com.ninty.classfile.ClassFile;
import com.ninty.classpath.ClassPath;
import com.ninty.runtime.LocalVars;
import com.ninty.runtime.heap.constantpool.NiConstant;
import com.ninty.runtime.heap.constantpool.NiConstantPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ninty on 2017/7/24.
 */
public class NiClassLoader {
    private ClassPath clzp;
    private Map<String, NiClass> classes;

    public NiClassLoader(ClassPath clzp) {
        this.clzp = clzp;
        classes = new HashMap<>(1 << 10);
    }

    public NiClass loadClass(String className) {
        if (classes.containsKey(className)) {
            return classes.get(className);
        }
        return loadNonArrayClass(className);
    }

    private NiClass loadNonArrayClass(String className) {
        byte[] datas = readClass(className);
        NiClass clz = definedClass(datas);
        classes.put(className, clz);
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
        clz.loader = this;
        resovleSuperClass(clz);
        resolveInterfaces(clz);
        return null;
    }

    private void resovleSuperClass(NiClass clz) {
        if (!clz.className.equals("java/lang/Object")) {
            clz.superClass = loadClass(clz.superClassName);
        }
    }

    private void resolveInterfaces(NiClass clz) {
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
                    throw new UnsupportedOperationException("unsupport String now");
            }
        }

    }
}
