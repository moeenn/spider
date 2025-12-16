package com.spider;

import java.net.URI;
import java.net.URL;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class CommandLineArgs {
    private final String URL_FLAG_NAME = "url";
    private final String REPORT_FILE_NAME_FLAG = "report";
    private final String MAX_PARALLEL_FLAG = "max-parallel";
    private final String HELP_FLAG = "help";

    private final URL url;
    private Optional<String> reportFileName = Optional.empty();
    private int maxParallel = 4;

    public CommandLineArgs(String[] args) throws Exception {
        final Options options = new Options();
        options.addOption(URL_FLAG_NAME, true, "Starting url to begin the spider");
        options.addOption(REPORT_FILE_NAME_FLAG, true, "Report file name");
        options.addOption(MAX_PARALLEL_FLAG, true, "Maximum number of parallel requests");
        options.addOption(HELP_FLAG, false, "Display program help");

        var parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        if (commandLine.hasOption(HELP_FLAG)) {
            HelpFormatter formatter = new HelpFormatter();
            String cmdLineSyntax = "spider [options]";
            formatter.printHelp(cmdLineSyntax, null, options, null);
            System.exit(0);
        }

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
                reportFileName = Optional.of(commandLine.getOptionValue(REPORT_FILE_NAME_FLAG));
            }

            if (commandLine.hasOption(MAX_PARALLEL_FLAG)) {
                maxParallel = Integer.parseInt(commandLine.getOptionValue(MAX_PARALLEL_FLAG));
            }
        } catch (Exception ex) {
            throw new Exception("invalid url argument: " + ex.getMessage());
        }
    }

    public URL getURL() {
        return url;
    }

    public Optional<String> getReportName() {
        return reportFileName;
    }

    public int getMaxParallel() {
        return maxParallel;
    }

    @Override
    public String toString() {
        if (reportFileName.isEmpty()) {
            return "Settings = Output: Console, MaxParallel: " + maxParallel;
        }

        return "Settings = Output (file): " + reportFileName.get() + ", MaxParallel: " + maxParallel;
    }
}
