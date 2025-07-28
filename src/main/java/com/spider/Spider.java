package com.spider;

import java.net.URL;
import java.util.HashSet;
import java.util.Map;
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

        for (QueueEntry url : urls.values()) {
            if (!url.getStatus().equals(QueueEntryStatus.PENDING)) {
                continue;
            }

            try {
                ProcessResult pageURLs = processor.process(url.getUrl());
                for (URL newURL : pageURLs.urls()) {
                    urls.put(newURL.toString(), new QueueEntry(newURL));
                }

                skipped.addAll(pageURLs.skipped());
                url.setStatus(QueueEntryStatus.COMPLETED);
            } catch (Exception ex) {
                url.setStatus(QueueEntryStatus.ERRORED);
            }
        }

        processQueue();
    }

    public void run() {
        processQueue();
    }

    public String getReport() {
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
