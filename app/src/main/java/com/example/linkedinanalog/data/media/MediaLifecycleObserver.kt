package com.example.linkedinanalog.data.media

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner


class MediaLifecycleObserver : LifecycleEventObserver {

    var isPlayed = false
    var musicNow: String = ""


    var mediaPlayer: MediaPlayer? = MediaPlayer()


    fun play(url:String) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                this.setDataSource(url)
            }

        }
        mediaPlayer?.setOnPreparedListener {
            it.start()
        }
        mediaPlayer?.prepareAsync()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                mediaPlayer?.pause()
            }
            Lifecycle.Event.ON_STOP -> {
                mediaPlayer?.release()
                mediaPlayer = null
            }
            Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)


            else -> Unit
        }
    }



}