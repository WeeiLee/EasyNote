package com.example.easynote.service.local.audio

import android.content.Context
import android.media.MediaRecorder

class AudioRepository private constructor(
    private val context: Context
) {
    companion object {
        @Volatile private var INSTANCE: AudioRepository? = null

        fun getInstance(context: Context): AudioRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AudioRepository(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
    private var recorder: MediaRecorder? = null
    private var filePath: String = ""

    fun startRecording() {
        filePath = "${context.filesDir}/audio_${System.currentTimeMillis()}.3gp"

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(filePath)
            prepare()
            start()
        }
    }

    fun stopRecording(): String {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        return filePath
    }
}
