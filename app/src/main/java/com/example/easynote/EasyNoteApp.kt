package com.example.easynote

import android.app.Application
import android.util.Log
import com.example.easynote.service.local.database.DatabaseManager

class EasyNoteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseManager.initialize(this)
        Log.d("APP", "EasyNoteApp started")
    }
}
