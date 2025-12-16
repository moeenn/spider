package com.spider;

public enum QueueEntryStatus {
    INPROGRESS("In-progress"),
    PENDING("Pending"),
    COMPLETED("Completed"),
    ERRORED("Errored"),
    SKIPPED("Skipped");

    private final String description;

    QueueEntryStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
