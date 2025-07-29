package com.spider;

import java.net.URI;
import java.net.URL;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class CommandLineArgs {
    private final URL url;
    private String reportFileName = "report.csv";
    private final String URL_FLAG_NAME = "url";
    private final String REPORT_FILE_NAME_FLAG = "report";

    public CommandLineArgs(String[] args) throws Exception {
        final Options options = new Options();
        options.addOption(URL_FLAG_NAME, true, "Starting url to beging the spider");
        options.addOption(REPORT_FILE_NAME_FLAG, true, "Report file name");

        var parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        if (!commandLine.hasOption(URL_FLAG_NAME)) {
            throw new Exception("Missing url argument");
        }

        try {
            String rawURL = commandLine.getOptionValue(URL_FLAG_NAME);
            if (!rawURL.endsWith("/")) {
                rawURL += "/";
            }
            url = new URI(rawURL).toURL();

            if (commandLine.hasOption(REPORT_FILE_NAME_FLAG)) {
                reportFileName = commandLine.getOptionValue(REPORT_FILE_NAME_FLAG);
            }
        } catch (Exception ex) {
            throw new Exception("Invalid url argument: " + ex.getMessage());
        }
    }

    public URL getURL() {
        return url;
    }

    public String getReportName() {
        return reportFileName;
    }
}
