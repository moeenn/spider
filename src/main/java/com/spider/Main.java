package com.spider;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        String report = spider.getReport();
        Path filePath = Paths.get("report.csv");
        try {
            Files.writeString(filePath, report);
        } catch (Exception ex) {
            System.err.printf("error: failed to write report file.");
            System.exit(1);
            return;
        }

        System.out.println("Report written to report.csv");
    }
}