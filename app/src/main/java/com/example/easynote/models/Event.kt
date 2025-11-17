package com.example.easynote.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Event(private val content:String, private val audio: Audio) : Category {
    private val timestamp: LocalDateTime = LocalDateTime.now()

    override fun getTimestamp(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return timestamp.format(formatter)
    }

    override fun getContent(): String {
        return content
    }

    override fun getAudio(): Audio {
        return audio
    }

}