package com.example.linkedinanalog.data.models.event

import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.Coordinates
import com.example.linkedinanalog.data.models.EventType

data class EventCreateRequest(
    val id: Int,
    val content: String,
    val dateTime: String?,
    val coords: Coordinates? = null,
    val type: EventType,
    val attachment: Attachment?,
    val link: String?,
    val speakerIds: List<Int>?,
)
