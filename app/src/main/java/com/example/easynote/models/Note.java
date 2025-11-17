package com.example.easynote.models;

import java.util.Map;

// record
public class Note {
    private final Integer id; // id so user can decide how to order it
    private final String title;
    private final Map<String, Object> content;
    private final String originalContent; // original content before any processing
    private final String summary; // summary of the note
    private final String timestamp; // timestamp to sort by time


    public Note(Integer id, String originalContent, String summary, String title, Map<String, Object> content, String timestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.originalContent = originalContent;
        this.summary = summary;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getId() {
        return id;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public String getSummary() {
        return summary;
    }
}
