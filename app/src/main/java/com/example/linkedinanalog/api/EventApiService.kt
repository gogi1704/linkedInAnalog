package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.event.EventCreateRequest
import com.example.linkedinanalog.data.models.event.EventModel
import retrofit2.Response
import retrofit2.http.*

interface EventApiService {


    @GET("/api/events/latest/")
    suspend fun getLatest(@Query("count") count :Int): Response<List<EventModel>>

    @GET("/api/events/{event_id}/after/")
    suspend fun getAfter( @Path("event_id") id:Long , @Query("count") count: Int): Response<List<EventModel>>

    @GET("/api/events/{event_id}/before/")
    suspend fun getBefore( @Path("event_id") id:Long , @Query("count") count: Int ): Response<List<EventModel>>

    @POST("/api/events/")
    suspend fun createEvent(@Body event: EventCreateRequest): Response<EventModel>

    @DELETE("/api/events/{event_id}/")
    suspend fun deleteEvent(@Path("event_id") id:Long):Response<Unit>

    @POST("/api/events/{event_id}/participants/")
    suspend fun participantByMeTrue(@Path("event_id") id: Long): Response<EventModel>

    @DELETE("/api/events/{event_id}/participants/")
    suspend fun participantByMeFalse(@Path("event_id") id: Long): Response<EventModel>

}