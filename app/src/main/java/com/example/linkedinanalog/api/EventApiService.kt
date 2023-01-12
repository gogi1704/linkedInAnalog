package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.event.EventModel
import retrofit2.Response
import retrofit2.http.GET

interface EventApiService {

    @GET("/api/events/")
    suspend fun getAllEvents(): Response<List<EventModel>>
}