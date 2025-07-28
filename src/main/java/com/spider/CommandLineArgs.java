package com.spider;

import java.net.URI;
import java.net.URL;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class CommandLineArgs {
    private final URL url;
    private final String URL_FLAG_NAME = "url";

    public CommandLineArgs(String[] args) throws Exception {
        final Options options = new Options();
        options.addOption(URL_FLAG_NAME, true, "Starting url to beging the spider");
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
        } catch (Exception ex) {
            throw new Exception("Invalid url argument: " + ex.getMessage());
        }
    }

    public URL getURL() {
        return url;
    }
}
