package com.spider;

import java.net.URL;
import java.util.Optional;
import com.spider.reporter.CSVReporter;
import com.spider.reporter.JsonReporter;
import com.spider.reporter.Reporter;
import com.spider.reporter.SupportedReportType;

public class Main {
    private static final Reporter defaultReporter = new CSVReporter();

    private static void run(String[] args) throws Exception {
        CommandLineArgs commandLineArgs;
        commandLineArgs = new CommandLineArgs(args);
        Log.println(commandLineArgs.toString());

        URL url = commandLineArgs.getURL();
        Spider spider = new Spider(commandLineArgs.getMaxParallel());

        Optional<String> reportName = commandLineArgs.getReportName();
        if (!reportName.isPresent()) {
            spider.run(url);
            String reportContent = spider.getReport(defaultReporter);
            System.out.printf("\n%s\n", reportContent);
            return;
        }

        SupportedReportType reportType = Reporter.getReportType(reportName.get());
        Reporter reporter = switch (reportType) {
            case CSV -> new CSVReporter();
            case JSON -> new JsonReporter();
        };

        spider.run(url);
        String reportContent = spider.getReport(reporter);
        Reporter.saveReport(reportContent, reportName.get());
        Log.printf("Report written to %s\n", reportName.get());
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
