package com.example.easynote.models


interface Category {

    fun getTimestamp(): String
    fun getContent(): String
    fun getAudio(): Audio
}