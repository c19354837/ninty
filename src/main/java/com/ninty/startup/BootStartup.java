package com.ninty.startup;

import com.ninty.classpath.ClassPath;
import com.ninty.runtime.NiThread;
import com.ninty.runtime.Slot;
import com.ninty.runtime.heap.NiClass;
import com.ninty.runtime.heap.NiClassLoader;
import com.ninty.runtime.heap.NiMethod;
import com.ninty.runtime.heap.NiObject;

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

    public BootStartup(String userCP, String className) {
        this.className = className;
        cp = new ClassPath(null, userCP);
        resolveClass();
    }

    //TODO -XX
    private void resolveClass() {
        loader = new NiClassLoader(cp);
        NiClass clz = loader.loadClass(className);
        NiMethod mainMethod = clz.getMainMethod();
        if (mainMethod == null) {
            throw new RuntimeException("Can't find main method in [" + className + "]");
        }

        NiThread thread = prepare();
        thread.execMethod(mainMethod);
    }

    private NiThread prepare() {
        NiThread thread = new NiThread(64, loader);
        NiObject threadGroup = newThreadGroup(thread);
        thread.generateThread(threadGroup, loader, "main");
        return thread;
    }

    private NiObject newThreadGroup(NiThread thread) {
        NiClass clz = loader.loadClass(CLZ_THREAD_GROUP);
        NiObject threadGroup = clz.newObject();
        NiMethod constructor = clz.getDefaultInitMethod();
        thread.execMethod(constructor, new Slot(threadGroup));
        return threadGroup;
    }
}
