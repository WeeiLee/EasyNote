package com.example.easynote.models;

import java.util.Map;

public class Note {
    String id;
    String title;
    Map<String, Object> content;
    String timestamp;

    public Note(String id, String title, Map<String, Object> content, String timestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
