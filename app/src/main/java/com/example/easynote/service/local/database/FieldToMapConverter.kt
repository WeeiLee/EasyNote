package com.example.easynote.service.local.database

import androidx.room.TypeConverter
import com.example.easynote.models.FieldType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FieldTypeMapConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromMap(map: Map<String?, FieldType>?): String? {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toMap(json: String?): Map<String?, FieldType>? {
        if (json == null) return null
        val type = object : TypeToken<Map<String?, FieldType>?>() {}.type
        return gson.fromJson(json, type)
    }
}
