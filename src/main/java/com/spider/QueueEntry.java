package com.spider;

import java.net.URL;
import java.time.Duration;
import java.util.Optional;

public class QueueEntry {
    private final URL url;
    private Optional<Duration> elapsed;
    private QueueEntryStatus status;
    private Optional<String> remarks;

    public QueueEntry(URL url) {
        this.url = url;
        this.elapsed = Optional.empty();
        this.status = QueueEntryStatus.PENDING;
        this.remarks = Optional.empty();
    }

    public URL getUrl() {
        return url;
    }

    public Optional<Duration> getElapsed() {
        return elapsed;
    }

    public void setElapsed(Duration elapsed) {
        this.elapsed = Optional.of(elapsed);
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

    public Optional<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = Optional.of(remarks);
    }

    public void setErrorStatus(Exception ex) {
        this.status = QueueEntryStatus.ERRORED;
        this.remarks = Optional.of(ex.getMessage());
    }
}
