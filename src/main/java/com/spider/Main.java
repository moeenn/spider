package com.spider;

import java.net.URL;
import java.util.Optional;
import com.spider.reporter.CSVReporter;
import com.spider.reporter.Reporter;

public class Main {
    private static void run(String[] args) throws Exception {
        CommandLineArgs commandLineArgs;
        commandLineArgs = new CommandLineArgs(args);
        System.out.println(commandLineArgs);

        URL url = commandLineArgs.getURL();
        Spider spider = new Spider(commandLineArgs.getMaxParallel());
        spider.run(url);

        Reporter csvReporter = new CSVReporter();
        String reportContent = spider.getReport(csvReporter);

        Optional<String> reportName = commandLineArgs.getReportName();
        if (reportName.isPresent()) {
            Reporter.saveReport(reportContent, reportName.get());
            System.out.printf("Report written to %s\n", reportName);
            return;
        }

        String separator = "-";
        System.out.printf("\n%s\n%s", separator.repeat(50), reportContent);
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