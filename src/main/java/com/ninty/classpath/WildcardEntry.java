package com.ninty.classpath;

import java.io.File;

/**
 * Created by ninty on 2017/7/8.
 */
public class WildcardEntry extends ClassEntry {

    private ClassEntry[] entrys;

    protected WildcardEntry(String path) {
        super(path);
        String basePath = path.substring(0, path.length() - 1);//remove *
        File[] files = new File(basePath).listFiles((dir, name) -> name.toLowerCase().endsWith(".zip") || name
                .toLowerCase().endsWith("jar"));
        entrys = new ClassEntry[files.length];
        for (int i = 0; i < files.length; i++) {
            entrys[i] = new zipEntry(files[i].getAbsolutePath());
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
