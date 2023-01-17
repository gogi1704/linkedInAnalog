package com.example.linkedinanalog.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attachment(
    val url: String?,
    val type: AttachmentType?
) : Parcelable

enum class AttachmentType {
    IMAGE,
    VIDEO
}
