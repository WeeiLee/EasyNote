package com.example.easynote.service.local.audio

import androidx.lifecycle.ViewModel

class AudioViewModel(
    private val audioRepository: AudioRepository
) : ViewModel() {

    fun startRecording() {
        audioRepository.startRecording()
    }

    fun stopRecording(): String {
        return audioRepository.stopRecording()
    }
}