package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.event.EventCreateRequest
import com.example.linkedinanalog.data.models.event.EventModel
import retrofit2.Response
import retrofit2.http.*

interface EventApiService {

    @GET("/api/events/")
    suspend fun getAllEvents(): Response<List<EventModel>>


    @POST("/api/events/")
    suspend fun createEvent(@Body event: EventCreateRequest): Response<EventModel>

    @POST("/api/events/{event_id}/participants/")
    suspend fun participantByMeTrue(@Path("event_id") id:Long):Response<EventModel>

    @DELETE("/api/events/{event_id}/participants/")
    suspend fun participantByMeFalse(@Path("event_id") id:Long):Response<EventModel>
}