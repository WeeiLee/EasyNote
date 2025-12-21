package com.example.easynote.models;

import java.util.Map;

public class NoteTable {
    private final Integer id;
    private final String title;
    private final String description;
    private final Map<String, FieldType> types;

    public NoteTable(Integer id, String title, String description, Map<String, FieldType> types) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.types = types;
    }
    // Types: Map of column name to data type (e.g., "id" -> "INTEGER", "name" -> "TEXT")
    // Data type must be an Object, not a primitive type

    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Map<String, FieldType> getTypes() {
        return types;
    }
}
