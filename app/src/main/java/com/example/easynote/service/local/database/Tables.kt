package com.example.easynote.service.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easynote.models.FieldType

@Entity
data class Tables (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val title: String? = null,
    val description: String? = null,
    val types: Map<String?, FieldType?>? = null
)