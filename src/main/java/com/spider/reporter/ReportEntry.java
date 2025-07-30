package com.spider.reporter;

import java.util.Optional;
import com.spider.QueueEntry;
import com.spider.QueueEntryStatus;

public record ReportEntry(String url, QueueEntryStatus status, Optional<String> remarks) {
    public static ReportEntry fromQueueEntry(QueueEntry entry) {
        return new ReportEntry(entry.getUrl().toString(), entry.getStatus(), entry.getRemarks());
    }

    public String toJson() {
        if (remarks().isPresent()) {
            return String.format("{ \"url\": \"%s\", \"status\": \"%s\", \"remarks\": \"%s\" }", url, status, remarks().get());            
        }
            
        return String.format("{ \"url\": \"%s\", \"status\": \"%s\" }", url, status);
    }
}
