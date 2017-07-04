package com.ninty;

import org.apache.commons.cli.*;

/**
 * Created by ninty on 2017/7/4.
 */
public class Ninty {

    public static void main(String[] args){
        Options options = new Options();
        options.addOption("help", "show the help");
        options.addOption("?", "show the help");
        options.addOption("classpath", "classpath");
        options.addOption("cp", "classpath");
        options.addOption("version", "version");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            printUsage(options);
            return;
        }

        if(commandLine.hasOption("version")){
            System.out.println("ninty 1.0.0");
        }else{
            printUsage(options);
        }
    }

    private static void printUsage(Options options){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ninty", options);
    }
}
