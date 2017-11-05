package com.ninty.startup;

import com.ninty.classpath.ClassPath;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.Slot;
import com.ninty.runtime.heap.*;

/**
 * Created by ninty on 2017/7/5.
 */
public class BootStartup {

    /**
     * classpath
     */
    private String className;
    private ClassPath cp;
    private NiClassLoader loader;

    private static final String CLZ_THREAD_GROUP = "java/lang/ThreadGroup";

    public BootStartup(String userCP, String className, String[] args) {
        this.className = className;
        cp = new ClassPath(null, userCP);
        resolveClass(args);
    }

    //TODO -XX
    private void resolveClass(String[] args) {
        loader = new NiClassLoader(cp);
        NiClass clz = loader.loadClass(className);
        NiMethod mainMethod = clz.getMainMethod();
        if (mainMethod == null) {
            throw new RuntimeException("Can't find main method in [" + className + "]");
        }

        NiThread mainThread = prepare();
        NiThread.setMainThread(mainThread.getCurrentThread());
        hackSystem();
        mainThread.getCurrentThread().setExtra(Boolean.TRUE);
        mainThread.execMethod(mainMethod, new Slot(getArgs(args)));
    }

    private NiObject getArgs(String[] args) {
        if (args == null) {
            return null;
        }
        NiClass astrClz = loader.loadClass("[java/lang/String;");
        NiObject astrObj = astrClz.newArray(args.length);
        NiObject[] arrayDatas = (NiObject[]) astrObj.getArrayDatas();
        for (int i = 0; i < arrayDatas.length; i++) {
            arrayDatas[i] = NiString.newString(loader, args[i]);
        }
        return astrObj;
    }

    private NiThread prepare() {
        NiThread thread = new NiThread(64);
        NiObject threadGroup = newThreadGroup(thread);
        thread.generateThread(threadGroup, loader, "main");
        return thread;
    }

    //    NiMethod metInitializeSystemClass = clzSys.getMethod("initializeSystemClass", "()V");
    //    NiThread.execMethodDirectly(metInitializeSystemClass);
    private void hackSystem() {
        NiClass clzSys = loader.loadClass("java/lang/System");
        NiClass clzPro = loader.loadClass("java/util/Properties");
        NiObject objPro = clzPro.newObject();
        clzSys.setStaticRef("props", "Ljava/util/Properties;", objPro);
        NiMethod init = clzPro.getMethod("<init>", "()V");
        NiThread.execMethodDirectly(init, new Slot(objPro));

//            NiMethod metInitializeSystemClass = clzSys.getMethod("initializeSystemClass", "()V");
//            NiThread.execMethodDirectly(metInitializeSystemClass);

    }

    private NiObject newThreadGroup(NiThread thread) {
        NiClass clz = loader.loadClass(CLZ_THREAD_GROUP);
        NiObject threadGroup = clz.newObject();
        NiMethod constructor = clz.getDefaultInitMethod();
        thread.execMethod(constructor, new Slot(threadGroup));
        return threadGroup;
    }
}
