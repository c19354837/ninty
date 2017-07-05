package com.ninty.startup;

/**
 * Created by ninty on 2017/7/5.
 */
public class BootStartup {

    /**
     * classpath
     */
    private String cp;
    private String cls;

    public BootStartup(String cp, String cls) {
        this.cp = cp;
        this.cls = cls;
        resolveClass();
    }

    private void resolveClass() {
        System.out.println("cp:" + cp + ", cls:" + cls);
    }
}
