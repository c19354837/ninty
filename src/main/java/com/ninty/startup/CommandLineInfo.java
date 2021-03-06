package com.ninty.startup;

import org.apache.commons.cli.*;

/**
 * Created by ninty on 2017/7/5.
 */
public class CommandLineInfo {

    public boolean version;
    public boolean help;
    public String cp;
    public String cls;
    public String[] args;

    private Options options = new Options();

    public CommandLineInfo(String[] args) {
        initOptions();
        fillCmd(args);
    }

    private void initOptions() {
        options.addOption("help", "show the help");
        options.addOption("?", "show the help");
        options.addOption("classpath", true, "classpath");
        options.addOption("cp", true, "classpath");
        options.addOption("version", "version");
    }

    private void fillCmd(String[] cmdArgs) {
        CommandLine cmd = getCommandLine(cmdArgs);

        if (cmd == null || cmd.hasOption("?") || cmd.hasOption("help")) {
            help = true;
            return;
        }
        if (cmd.hasOption("version")) {
            version = true;
            return;
        }
        if (cmd.hasOption("cp") || cmd.hasOption("classpath")) {
            cp = cmd.getOptionValue("cp", cmd.getOptionValue("classpath"));
        }
        String[] otherArgs = cmd.getArgs();
        if (otherArgs.length > 0) {
            cls = otherArgs[0];
        }
        if (otherArgs.length > 1) {
            args = new String[otherArgs.length - 1];
            for (int i = 0; i < args.length; i++) {
                args[i] = otherArgs[i + 1];
            }
        }
    }

    private CommandLine getCommandLine(String[] args) {
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            this.help = true;
        }
        return commandLine;
    }

    public void printVersion() {
        System.out.println("ninty 1.0.0");
    }

    public void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ninty [-options] class [args...]", options);
    }
}
