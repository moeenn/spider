package com.spider.reporter;

import java.util.List;
import java.util.Optional;

public class CSVReporter extends Reporter {
    @Override
    public String report(ReportArgs args) {
        List<ReportEntry> entries = args.getSortedUrls();
        StringBuilder builder = new StringBuilder();

        builder.append("URL,Status,Remarks\n");
        for (ReportEntry entry : entries) {
            builder.append(entry.url() + "," + entry.status() + ",");
            if (entry.remarks().isPresent()) {
                builder.append(entry.remarks().get());
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
