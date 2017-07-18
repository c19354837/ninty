package com.ninty.classpath;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by ninty on 2017/7/8.
 */
public class zipEntry extends ClassEntry {
    protected zipEntry(String path) {
        super(path);
    }

    @Override
    public byte[] readClass(String className) {
        ZipInputStream zin = null;
        try {
            ZipFile zf = isJar() ? new JarFile(path) : new ZipFile(path);
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().equals(className)) {
                    byte[] datas = new byte[(int) ze.getSize()];
                    IOUtils.readFully(zf.getInputStream(ze), datas);
                    return datas;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zin);
        }
        return null;
    }

    private boolean isJar() {
        return path.toLowerCase().endsWith(".jar");
    }
}
