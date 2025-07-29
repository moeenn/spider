package com.spider;

import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.spider.URLProcessor.ProcessResult;

public class Spider {
    private Map<String, QueueEntry> urls;
    private Set<String> skipped;
    private URLProcessor processor;

    public Spider(URL startURL) {
        this.processor = new URLProcessor();
        this.skipped = new HashSet<>();
        this.urls = new ConcurrentHashMap<String, QueueEntry>() {
            {
                put(startURL.toString(), new QueueEntry(startURL));
            }
        };
    }

    private void processQueue() {
        boolean anyPending = urls.values().stream().anyMatch(QueueEntry::isPending);
        if (!anyPending) {
            return;
        }

        for (QueueEntry entry : urls.values()) {
            if (entry.getStatus().equals(QueueEntryStatus.PENDING)) {
                try {
                    Optional<ProcessResult> pageURLs = processor.process(entry.getUrl());
                    if (!pageURLs.isPresent()) {
                        entry.setStatus(QueueEntryStatus.STATIC_ASSET);
                        continue;
                    }

                    entry.setStatus(QueueEntryStatus.COMPLETED);
                    skipped.addAll(pageURLs.get().skipped());
                    for (URL newURL : pageURLs.get().urls()) {
                        if (urls.containsKey(newURL.toString())) {
                            continue;
                        } else {
                            urls.put(newURL.toString(), new QueueEntry(newURL));
                        }
                    }
                } catch (Exception ex) {
                    entry.setStatus(QueueEntryStatus.ERRORED);
                }
            }
        }

        processQueue();
    }

    public void run() {
        processQueue();
    }

    public String getCSVReport() {
        StringBuilder builder = new StringBuilder();
        builder.append("URL,Status\n");

        for (QueueEntry entry : urls.values()) {
            builder.append(entry.getUrl() + "," + entry.getStatus() + "\n");
        }

        for (String sk : skipped) {
            builder.append(sk + "," + "SKIPPED\n");
        }

        return builder.toString();
    }
}
