package com.ninty.classpath;

/**
 * Created by ninty on 2017/7/8.
 */
public abstract class ClassEntry {

    protected String path;

    protected ClassEntry(String path) {
        this.path = path;
    }

    public abstract byte[] readClass(String className);
}
