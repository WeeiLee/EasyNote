package com.example.easynote.service.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromMap(map: Map<String?, Any?>?): String? {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toMap(json: String?): Map<String?, Any?>? {
        if (json == null) return null
        val type = object : TypeToken<Map<String?, Any?>?>() {}.type
        return gson.fromJson(json, type)
    }
}
