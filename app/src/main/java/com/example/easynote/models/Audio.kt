package com.example.easynote.models

import android.media.MediaPlayer

class Audio(private val path: String) {

    private var player: MediaPlayer? = null

    fun play(onCompleted: () -> Unit = {}) {
        if (player != null) return

        player = MediaPlayer().apply {
            setDataSource(path)
            prepare()
            start()
            // cuando termina libera recurso y puede pasar la funcion para que cambie icono de play
            setOnCompletionListener {
                stop()
                onCompleted()
            }
        }
    }

    fun stop() {
        player?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        player = null
    }

    fun isPlaying(): Boolean {
        return player?.isPlaying == true
    }
}