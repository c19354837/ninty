package com.ninty.classpath;

import java.io.File;

/**
 * Created by ninty on 2017/7/8.
 */
public class CompositeEntry extends ClassEntry {

    private ClassEntry[] entrys;

    protected CompositeEntry(String path) {
        super(path);
        String[] paths = path.split(File.pathSeparator);
        entrys = new ClassEntry[paths.length];
        for (int i = 0; i < paths.length; i++) {
            entrys[i] = ClassEntryFactory.getEntry(paths[i]);
        }

    }

    @Override
    public byte[] readClass(String className) {
        for (ClassEntry entry : entrys) {
            byte[] result = entry.readClass(className);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
