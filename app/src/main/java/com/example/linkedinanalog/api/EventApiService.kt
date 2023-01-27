package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.event.EventCreateRequest
import com.example.linkedinanalog.data.models.event.EventModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventApiService {

    @GET("/api/events/")
    suspend fun getAllEvents(): Response<List<EventModel>>


    @POST("/api/events/")
    suspend fun createEvent(@Body event: EventCreateRequest): Response<EventModel>
}