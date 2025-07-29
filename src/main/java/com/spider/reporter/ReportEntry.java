package com.spider.reporter;

import com.spider.QueueEntry;
import com.spider.QueueEntryStatus;

public record ReportEntry(String url, QueueEntryStatus status) {
    public static ReportEntry fromQueueEntry(QueueEntry entry) {
        return new ReportEntry(entry.getUrl().toString(), entry.getStatus());
    }

    public String toJson() {
        return String.format("{ \"url\": \"%s\", \"status\": \"%s\" }", url, status);
    }
}
