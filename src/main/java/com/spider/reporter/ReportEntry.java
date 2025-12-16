package com.spider.reporter;

import java.time.Duration;
import java.util.Optional;
import com.spider.QueueEntry;
import com.spider.QueueEntryStatus;

public record ReportEntry(String url, Optional<Duration> elapsed, QueueEntryStatus status, Optional<String> remarks) {
    public static ReportEntry fromQueueEntry(QueueEntry entry) {
        return new ReportEntry(entry.getUrl().toString(), entry.getElapsed(), entry.getStatus(), entry.getRemarks());
    }

    public String toJson() {
        if (remarks().isPresent()) {
            return String.format("{ \"url\": \"%s\", \"elapsed\": \"%s\", \"status\": \"%s\", \"remarks\": \"%s\" }",
                    url,
                    elapsed().map(Duration::toString).orElse(""),
                    status,
                    remarks().get());
        }

        return String.format("{ \"url\": \"%s\", \"elapsed\": \"%s\", \"status\": \"%s\" }",
                url,
                elapsed().map(Duration::toString).orElse(""),
                status);
    }
}
