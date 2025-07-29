package com.spider.reporter;

import java.util.List;

public class CSVReporter extends Reporter {
    @Override
    public String report(ReportArgs args) {
        List<ReportEntry> entries = args.getSortedUrls();
        StringBuilder builder = new StringBuilder();

        builder.append("URL,Status\n");
        for (ReportEntry entry : entries) {
            builder.append(entry.url() + "," + entry.status() + "\n");
        }

        return builder.toString();
    }
}
