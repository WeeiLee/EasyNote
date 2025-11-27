package com.example.easynote.service.local.audio

import android.content.Context
import androidx.lifecycle.ViewModel

class AudioViewModel : ViewModel() {

    private lateinit var audioRepository: AudioRepository

    fun insertRepository(rep: AudioRepository) {
        audioRepository = rep
    }

    fun startRecording(context: Context) {
        audioRepository.startRecording(context)
    }

    fun stopRecording(): String {
        return audioRepository.stopRecording()
    }
}
