package com.example.easynote.models;

import java.util.ArrayList;
import java.util.Map;

public class NoteTable {
    ArrayList<Note> rows;
    public NoteTable(String title, Map<String, String> types) {}
    // Types: Map of column name to data type (e.g., "id" -> "INTEGER", "name" -> "TEXT")
    // Data type must be an Object, not a primitive type

    // TODO: Add methods to manipulate the table (e.g., addRow, getRows, etc.)
}
