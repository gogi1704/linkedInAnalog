package com.example.linkedinanalog.data.models.event

import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.Coordinates
import com.example.linkedinanalog.data.models.EventType

data class EventCreateRequest(
    val id: Int,
    val content: String,
    val datetime: String? = null,
    val coords: Coordinates? = null,
    val type: EventType,
    val attachment: Attachment? = null,
    val link: String? = null,
    val speakerIds: List<Int>? = null,
)
