package com.ninty.runtime.heap;

import com.ninty.classfile.ClassFile;
import com.ninty.classpath.ClassPath;

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
    }
}
