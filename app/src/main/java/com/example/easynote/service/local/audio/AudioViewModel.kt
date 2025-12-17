package com.example.easynote.service.local.audio

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioViewModel(
    //private val audioRepository: AudioRepository = AudioRepository,
    private val speechRepository: SpeechRecognizerRepository = SpeechRecognizerRepository
) : ViewModel() {

    private val _text = MutableStateFlow("")
    var lastProcessedText: String? = null
    val text = _text.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening = _isListening.asStateFlow()

    fun configure(context: Context) {
        speechRepository.configure(context)
    }

    fun startRecording(context: Context) {
        _isListening.value = true

        //Iniciar la grabación
        //audioRepository.startRecording(context)
        //Iniciar transformador de texto pasando un Listener
        speechRepository.startListening(object : RecognitionListener {

            override fun onResults(results: Bundle?) {
                _text.value = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull() ?: ""

                _isListening.value = false

            }

            override fun onPartialResults(results: Bundle?) {
                /*_text.value = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull() ?: ""*/
            }

            override fun onError(error: Int) {
                when (error) {
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT,
                    SpeechRecognizer.ERROR_NO_MATCH -> {
                        _isListening.value = false
                        return
                    }
                    else -> {
                        _isListening.value = false
                    }
                }
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    fun stopRecording() {
        _isListening.value = false
        speechRepository.stopListening()
        //return audioRepository.stopRecording()
    }

    override fun onCleared() {
        super.onCleared()
        speechRepository.destroy()
    }
}
