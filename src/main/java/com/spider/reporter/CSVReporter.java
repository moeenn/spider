package com.spider.reporter;

import com.spider.QueueEntry;
import com.spider.QueueEntryStatus;

public class CSVReporter extends Reporter {
    @Override
    public String report(ReportArgs args) {
        StringBuilder builder = new StringBuilder();
        builder.append("URL,Status\n");

        for (QueueEntry entry : args.urls()) {
            builder.append(entry.getUrl() + "," + entry.getStatus() + "\n");
        }

        for (String sk : args.skipped()) {
            builder.append(sk + "," + QueueEntryStatus.SKIPPED.toString() + "\n");
        }

        return builder.toString();
    }
}
