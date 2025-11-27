package com.example.easynote.service.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val title: String?,
    val originalContent: String?,
    val summary: String?,
    val fields: Map<String?, Any?>?,
    val timestamp: String?,
    val noteTableId: Int
)