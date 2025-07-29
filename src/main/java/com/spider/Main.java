package com.spider;

import java.net.URL;
import com.spider.reporter.CSVReporter;
import com.spider.reporter.Reporter;

public class Main {
    private static void run(String[] args) throws Exception {
        CommandLineArgs commandLineArgs;
        commandLineArgs = new CommandLineArgs(args);

        URL url = commandLineArgs.getURL();
        Spider spider = new Spider(url);
        spider.run();

        Reporter csvReporter = new CSVReporter();
        String report = spider.getReport(csvReporter);
        Reporter.saveReport(report, commandLineArgs.getReportName());
        System.out.printf("Report written to %s\n", commandLineArgs.getReportName());
    }

    public static void main(String[] args) {
        try {
            Main.run(args);
        } catch (Exception ex) {
            System.err.printf("error: %s\n", ex.getMessage());
            System.exit(1);
        }
    }
}