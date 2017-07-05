package com.ninty;

import com.ninty.startup.BootStartup;
import com.ninty.startup.CommandLineInfo;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

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
            System.out.println("ninty 1.0.0");
        } else if (cmd.help || cmd.cls == null) {
            cmd.printUsage();
        } else {
            new BootStartup(cmd.cp, cmd.cls);
        }
    }

    private void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ninty [-options] class [args...]", options);
    }
}
