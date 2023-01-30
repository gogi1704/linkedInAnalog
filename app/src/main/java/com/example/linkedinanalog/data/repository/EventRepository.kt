package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.data.models.event.EventCreateRequest

interface EventRepository {
    suspend fun createEvent(item: EventCreateRequest)
    suspend fun deleteEvent(item: EventCreateRequest)
    suspend fun participantByMe(id: Long, isParticipatedByMe: Boolean)
}