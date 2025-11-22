package com.example.easynote.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easynote.service.local.audio.AudioRepository
import com.example.easynote.service.local.audio.AudioViewModel

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AudioViewModel::class.java)) {
            val vm = createAudioViewModel()

            @Suppress("UNCHECKED_CAST")
            return vm as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun createAudioViewModel(): AudioViewModel {
        val repo = AudioRepository.getInstance(context.applicationContext)
        return AudioViewModel(repo)
    }
}
