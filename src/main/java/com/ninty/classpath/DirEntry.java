package com.ninty.classpath;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by ninty on 2017/7/8.
 */
public class DirEntry extends ClassEntry {

    protected DirEntry(String path) {
        super(path);
    }

    public byte[] readClass(String className) {
        String classFile = path + className;
        File file = new File(classFile);
        if (file.exists()) {
            try {
                return FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
