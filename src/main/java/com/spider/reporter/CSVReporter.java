package com.spider.reporter;

import java.util.List;

public class CSVReporter extends Reporter {
    @Override
    public String report(ReportArgs args) {
        List<ReportEntry> entries = args.getSortedUrls();
        StringBuilder builder = new StringBuilder();

        builder.append("URL, Elapsed, Status, Remarks\n");
        for (ReportEntry entry : entries) {
            String elapsed = entry.elapsed()
                    .map((d) -> {
                        return String.format("%dms", d.toMillis());
                    })
                    .orElse("0ms");

            builder.append(entry.url() + ", " + elapsed + ", " + entry.status() + ", ");
            if (entry.remarks().isPresent()) {
                builder.append(entry.remarks().get());
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
