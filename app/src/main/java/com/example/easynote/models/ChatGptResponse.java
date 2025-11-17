package com.example.easynote.models;

import java.util.Map;

public class ChatGptResponse {
    private final String summary;
    private final String title;
    private final String table;
    private final String timestamp;
    private final Map<String, Object> fields;

    public ChatGptResponse(String title, String summary, String table, Map<String, Object> fields, String timestamp) {
        this.summary = summary;
        this.table = table;
        this.title = title;
        this.timestamp = timestamp;
        this.fields = fields;
    }

    public Map<String, Object> getFields() {
        return fields;
    }
    public String getSummary() {
        return summary;
    }
    public String getTitle() {
        return title;
    }
    public String getTable() {
        return table;
    }
    public String getTimestamp() {
        return timestamp;
    }

}
