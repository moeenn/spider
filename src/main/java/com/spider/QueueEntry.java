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

    public boolean isCompleted() {
        return status.equals(QueueEntryStatus.COMPLETED);
    }

    public boolean isPending() {
        return status.equals(QueueEntryStatus.PENDING);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        QueueEntry other = (QueueEntry) obj;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("(Status: %s, URL: %s)", status, url);
    }
}
