package com.ninty.startup;

import com.ninty.classpath.ClassPath;

/**
 * Created by ninty on 2017/7/5.
 */
public class BootStartup {

    /**
     * classpath
     */
    private String className;

    private ClassPath cp;

    public BootStartup(String userCP, String className) {
        this.className = className;
        cp = new ClassPath(null, userCP);
        resolveClass();
    }

    private void resolveClass() {
        System.out.println(cp.readClass(className).length);
    }

}
