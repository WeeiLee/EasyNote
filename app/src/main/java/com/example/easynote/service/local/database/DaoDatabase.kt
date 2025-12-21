package com.example.easynote.service.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Notes::class, Tables::class], version = 3)

@TypeConverters(MapConverter::class, FieldTypeMapConverter::class)
abstract class DaoDatabase: RoomDatabase() {
    abstract fun noteDao(): DaoNote
    abstract fun tableDao(): DaoTable
}