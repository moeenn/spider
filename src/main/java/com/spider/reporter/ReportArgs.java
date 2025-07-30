package com.spider.reporter;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.spider.QueueEntry;
import com.spider.QueueEntryStatus;

public record ReportArgs(List<QueueEntry> urls, Set<String> skipped) {
    public List<ReportEntry> getSortedUrls() {
        List<ReportEntry> entries = new ArrayList<>();
        for (QueueEntry entry : urls()) {
            entries.add(ReportEntry.fromQueueEntry(entry));
        }

        for (String sk : skipped()) {
            entries.add(new ReportEntry(sk, QueueEntryStatus.SKIPPED, Optional.of("")));
        }

        Collections.sort(entries, (ReportEntry a, ReportEntry b) -> a.url().compareTo(b.url()));
        return entries;
    }
}
