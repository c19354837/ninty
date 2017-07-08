package com.ninty.classpath;

import java.io.File;

/**
 * Created by ninty on 2017/7/8.
 */
public class ClassEntryFactory {

    private ClassEntryFactory() {
    }

    public static ClassEntry getEntry(String path) {
        if (path == null) {
            throw new NullPointerException("cannot set class path to null");
        }
        if (path.contains(File.pathSeparator)) {
            return new CompositeEntry(path);
        } else if (path.contains("*")) {
            return new WildcardEntry(path);
        } else if (path.toLowerCase().endsWith(".zip") || path.toLowerCase().endsWith("jar")) {
            return new zipEntry(path);
        }

        return new DirEntry(path);
    }
}
