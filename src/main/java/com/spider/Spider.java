package com.spider;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import com.spider.URLProcessor.ProcessResult;
import com.spider.reporter.ReportArgs;
import com.spider.reporter.Reporter;

public class Spider {
    private Map<String, QueueEntry> urls;
    private Set<String> skipped;
    private URLProcessor processor;
    private final int maxParallel;

    public Spider(int maxParallel) {
        this.processor = new URLProcessor();
        this.skipped = new HashSet<>();
        this.urls = new ConcurrentHashMap<String, QueueEntry>();
        this.maxParallel = maxParallel;
    }

    public boolean addQueueEntry(QueueEntry entry) {
        if (urls.containsKey(entry.getUrl().toString())) {
            return false;
        }

        urls.put(entry.getUrl().toString(), entry);
        return true;
    }

    private Callable<List<QueueEntry>> processEntry(QueueEntry entry) {
        return () -> {
            List<QueueEntry> results = new ArrayList<>();
            entry.setStatus(QueueEntryStatus.INPROGRESS);
            try {
                Optional<ProcessResult> processResults = processor.process(entry.getUrl());
                if (!processResults.isPresent()) {
                    entry.setStatus(QueueEntryStatus.SKIPPED);
                    entry.setRemarks("Static asset file.");
                    return results;
                }

                entry.setStatus(QueueEntryStatus.COMPLETED);
                skipped.addAll(processResults.get().skipped());
                for (URL newURL : processResults.get().urls()) {
                    QueueEntry newEntry = new QueueEntry(newURL);
                    results.add(newEntry);
                }
            } catch (Exception ex) {
                System.err.printf("error: %s: %s\n", entry.getUrl().toString(), ex.getMessage());
                entry.setErrorStatus(ex);
            }

            return results;
        };
    }

    public void run(URL startURL) throws Exception {
        QueueEntry startEntry = new QueueEntry(startURL);
        addQueueEntry(startEntry);

        try (ExecutorService executor = Executors.newFixedThreadPool(maxParallel,
                Thread.ofVirtual().factory())) {

            for (QueueEntry entry : urls.values()) {
                Future<List<QueueEntry>> newEntriesFuture = executor.submit(processEntry(entry));
                List<QueueEntry> newEntries = newEntriesFuture.get();
                for (QueueEntry newEntry : newEntries) {
                    boolean isAdded = addQueueEntry(newEntry);
                    if (isAdded) {
                        executor.submit(processEntry(newEntry));
                    }
                }
            }

            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.MINUTES);
        }
    }

    public String getReport(Reporter reporter) {
        ReportArgs args = new ReportArgs(
                urls.values().stream().toList(),
                skipped);

        return reporter.report(args);
    }
}
