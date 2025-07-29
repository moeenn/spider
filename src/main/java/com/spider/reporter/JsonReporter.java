package com.spider.reporter;

import java.util.List;

public class JsonReporter extends Reporter {
    @Override
    public String report(ReportArgs args) {
        StringBuilder builder = new StringBuilder();
        builder.append("{ \"urls\": [");

        List<ReportEntry> entries = args.getSortedUrls();
        for (int i = 0; i < entries.size(); i++) {
            builder.append(entries.get(i).toJson());

            if (i != (entries.size() - 1)) {
                builder.append(",");
            }
        }

        builder.append("]}");
        return builder.toString();
    }
}
