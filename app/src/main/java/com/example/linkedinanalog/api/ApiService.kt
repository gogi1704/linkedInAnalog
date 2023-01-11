package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.models.post.PostModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("/api/events/")
    suspend fun getEvents(): Response<List<EventModel>>
}