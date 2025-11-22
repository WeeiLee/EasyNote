package com.example.easynote.models;

import java.util.Map;

// record
public class Note {
    private final Integer id; // id so user can decide how to order it
    private final String title;
    private final Map<String, Object> fields;
    private final String originalContent; // original content before any processing
    private final String summary; // summary of the note
    private final Integer noteTableId;
    private final String timestamp; // timestamp to sort by time

    public Note(Integer id, String title, String summary, String originalContent, Map<String, Object> fields, Integer noteTableId, String timestamp) {
        this.id = id;
        this.title = title;
        this.fields = fields;
        this.originalContent = originalContent;
        this.summary = summary;
        this.timestamp = timestamp;
        this.noteTableId = noteTableId;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public Map<String, Object> getFields() {
        return fields;
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

    public Integer getNoteTableId() {
        return noteTableId;
    }
}
