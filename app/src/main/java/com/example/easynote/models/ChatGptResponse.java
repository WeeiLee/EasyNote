package com.example.easynote.models;

import java.util.Map;

public class ChatGptResponse {
    private final String summary;
    private final String title;
    private final Integer tableId;
    private final String timestamp;
    private final Map<String, Object> fields;

    public ChatGptResponse(Integer tableId, String title, String summary, Map<String, Object> fields, String timestamp) {
        this.tableId = tableId;
        this.title = title;
        this.summary = summary;
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
    public Integer getTableId() {
        return tableId;
    }
    public String getTimestamp() {
        return timestamp;
    }

}
