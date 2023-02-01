package com.example.linkedinanalog.data.media.mediaModels

import android.net.Uri
import java.io.File

data class AudioModel(
    val uri: Uri? = null,
    val file: File? = null,
    val isPlay:Boolean = false
)
