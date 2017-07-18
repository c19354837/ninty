package com.ninty.classpath;

import com.ninty.utils.VMUtils;

import java.io.File;

/**
 * Created by ninty on 2017/7/8.
 */
public class ClassPath {
    ClassEntry bootClassPath;
    ClassEntry extClassPath;
    ClassEntry appClassPath;

    public ClassPath(String javaHome, String classPath) {
        initBootAndExt(javaHome);
        initApp(classPath);
    }

    private void initBootAndExt(String javaHome) {
        if (VMUtils.isEmpty(javaHome)) {
            javaHome = getJavaHome();
        }
        String bootPath = String.join(File.separator, javaHome, "lib", "*");
        bootClassPath = ClassEntryFactory.getEntry(bootPath);

        String extPath = String.join(File.separator, javaHome, "lib", "ext", "*");
        extClassPath = ClassEntryFactory.getEntry(extPath);
    }

    private void initApp(String classPath) {
        if (!VMUtils.isEmpty(classPath)) {
            appClassPath = ClassEntryFactory.getEntry(classPath);
        }
    }

    private String getJavaHome() {
        String javaHome = System.getProperty("java.home");
        if (!new File(javaHome).exists()) {
            throw new Error("Cannot find jre path");
        }
        return javaHome;
    }

    public byte[] readClass(String className) {
        String realCN = convert(className);
        byte[] datas = bootClassPath.readClass(realCN);
        if (datas == null) {
            datas = extClassPath.readClass(realCN);
        }
        if (datas == null && appClassPath != null) {
            datas = appClassPath.readClass(realCN);
        }
        return datas;
    }

    private String convert(String className) {
        return className.replace('.', File.separatorChar) + ".class";
    }
}
