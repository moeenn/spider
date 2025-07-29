package com.spider.reporter;

import java.util.List;
import java.util.Set;
import com.spider.QueueEntry;

public record ReportArgs(List<QueueEntry> urls, Set<String> skipped) {
}
