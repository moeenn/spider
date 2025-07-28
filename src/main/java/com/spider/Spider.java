package com.spider;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import com.spider.URLProcessor.ProcessResult;

public class Spider {
    private Set<URL> urls;
    private Set<String> skipped;
    private URLProcessor processor;

    public Spider(URL startURL) {
        this.processor = new URLProcessor();
        this.skipped = new HashSet<>();
        this.urls = new HashSet<URL>() {
            {
                add(startURL);
            }
        };
    }

    private void processQueue() {
        for (URL url : urls) {
            try {
                ProcessResult pageURLs = processor.process(url);
                urls.addAll(pageURLs.urls());
                skipped.addAll(pageURLs.skipped());
            } catch (Exception ex) {
                System.err.printf("error: %s\n", ex.getMessage());
            }
        }
    }

    public void run() {
        processQueue();

        // TODO: remove after testing.
        System.out.println("URLs :: ");
        for (URL url : urls) {
            System.out.println(url);
        }

        System.out.printf("\n\nSkipped ::\n");
        for (String s : skipped) {
            System.out.println(s);
        }
    }
}
