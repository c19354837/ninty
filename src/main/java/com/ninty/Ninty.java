package com.ninty;

import com.ninty.startup.BootStartup;
import com.ninty.startup.CommandLineInfo;

/**
 * Created by ninty on 2017/7/4.
 */
public class Ninty {

    public static void main(String[] args) {
        new Ninty().resolveArgs(args);
    }

    private void resolveArgs(String[] args) {
        CommandLineInfo cmd = new CommandLineInfo(args);
        if (cmd.version) {
            cmd.printVersion();
        } else if (cmd.help || cmd.cls == null) {
            cmd.printUsage();
        } else {
            new BootStartup(cmd.cp, cmd.cls, cmd.args);
        }
    }
}
