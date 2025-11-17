package com.example.easynote.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class NoteTable {
    private final String title;
    private final String description;
    private final Map<String, FieldType> types;
    private final ArrayList<Note> notes = new ArrayList<>();

    public NoteTable(String title, String description, Map<String, FieldType> types) {
        this.title = title;
        this.description = description;
        this.types = types;
    }
    // Types: Map of column name to data type (e.g., "id" -> "INTEGER", "name" -> "TEXT")
    // Data type must be an Object, not a primitive type

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Note> getNotesSortByTimestampDesc() {
        notes.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return notes;
    }

    public ArrayList<Note> getNotesSortByTimestampAsc() {
        notes.sort(Comparator.comparing(Note::getTimestamp));
        return notes;
    }

    public ArrayList<Note> getNotesSortByIdDesc() {
        notes.sort((a, b) -> b.getId().compareTo(a.getId()));
        return notes;
    }

    public ArrayList<Note> getNotesSortByIdAsc() {
        notes.sort(Comparator.comparing(Note::getId));
        return notes;
    }

    public Map<String, FieldType> getTypes() {
        return types;
    }

    public String getTitle() {
        return title;
    }

    public void addNote(Note note) {
        // Check for duplicate ids
        for (Note n : notes) {
            if (n.getId().equals(note.getId())) {
                throw new IllegalArgumentException("Duplicate id: " + note.getId());
            }
        }

        this.notes.add(note);
    }
}
