package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.linkedinanalog.data.media.mediaModels.PlayState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

     var playState = PlayState()
        set(value) {
            field = value
            _playStateLiveData.value = value
        }

    private val _playStateLiveData = MutableLiveData(playState)
    val playStateLiveData
        get() = _playStateLiveData
}