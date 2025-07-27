package com.spider;

import java.net.URL;

public class Main {
    public static void main(String[] args) {
        CommandLineArgs commandLineArgs;
        try {
            commandLineArgs = new CommandLineArgs(args);
        } catch (Exception ex) {
            System.err.printf("error: %s\n", ex.getMessage());
            System.exit(1);
            return;
        }

        URL url = commandLineArgs.getURL();
        Spider spider = new Spider(url);
        spider.run();
    }
}