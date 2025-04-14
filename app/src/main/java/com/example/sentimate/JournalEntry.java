package com.example.sentimate;

public class JournalEntry {
    private String text;
    private String timestamp;

    public JournalEntry(String text, String timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

