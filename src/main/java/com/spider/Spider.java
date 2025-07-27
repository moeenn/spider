package com.spider;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Spider {
    private Queue<URL> urls;
    private URLProcessor processor;

    public Spider(URL startURL) {
        this.processor = new URLProcessor();
        this.urls = new LinkedList<URL>() {
            {
                push(startURL);
            }
        };
    }

    private void processQueue() {
        for (URL url : urls) {
            try {
                List<URL> pageURLs = processor.process(url);
                urls.addAll(pageURLs);
            } catch (Exception ex) {
                System.err.printf("error: %s\n", ex.getMessage());
            }
        }
    }

    public void run() {
        processQueue();
    }
}
