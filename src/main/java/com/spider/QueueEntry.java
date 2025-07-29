package com.spider;

import java.net.URL;

public class QueueEntry {
    private final URL url;
    private QueueEntryStatus status;

    public QueueEntry(URL url) {
        this.url = url;
        this.status = QueueEntryStatus.PENDING;
    }

    public URL getUrl() {
        return url;
    }

    public QueueEntryStatus getStatus() {
        return status;
    }

    public void setStatus(QueueEntryStatus status) {
        this.status = status;
    }

    public boolean isPending() {
        return status.equals(QueueEntryStatus.PENDING);
    }
}
