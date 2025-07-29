package com.spider;

import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;

import com.spider.URLProcessor.ProcessResult;
import com.spider.reporter.ReportArgs;
import com.spider.reporter.Reporter;

public class Spider {
    private Map<String, QueueEntry> urls;
    private Set<String> skipped;
    private URLProcessor processor;
    // private int maxParallel = 4;
    // private ExecutorService executor;

    public Spider(URL startURL) {
        this.processor = new URLProcessor();
        this.skipped = new HashSet<>();
        this.urls = new ConcurrentHashMap<String, QueueEntry>() {
            {
                put(startURL.toString(), new QueueEntry(startURL));
            }
        };

        // executor = Executors.newFixedThreadPool(maxParallel,
        // Thread.ofVirtual().factory());
    }

    private void processQueue() {
        boolean anyPending = urls.values().stream().anyMatch(QueueEntry::isPending);
        if (!anyPending) {
            return;
        }

        for (QueueEntry entry : urls.values()) {
            if (!entry.getStatus().equals(QueueEntryStatus.PENDING)) {
                continue;
            }

            try {
                Optional<ProcessResult> pageURLs = processor.process(entry.getUrl());
                if (!pageURLs.isPresent()) {
                    entry.setStatus(QueueEntryStatus.STATIC);
                    continue;
                }

                entry.setStatus(QueueEntryStatus.COMPLETED);
                skipped.addAll(pageURLs.get().skipped());
                for (URL newURL : pageURLs.get().urls()) {
                    if (!urls.containsKey(newURL.toString())) {
                        urls.put(newURL.toString(), new QueueEntry(newURL));
                    }
                }
            } catch (Exception ex) {
                entry.setStatus(QueueEntryStatus.ERRORED);
            }
        }

        processQueue();
    }

    public void run() {
        processQueue();
    }

    public String getReport(Reporter reporter) {
        ReportArgs args = new ReportArgs(
                urls.values().stream().toList(),
                skipped);

        return reporter.report(args);
    }
}
