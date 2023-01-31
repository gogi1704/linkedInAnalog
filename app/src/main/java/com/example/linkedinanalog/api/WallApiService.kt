package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.post.PostModel
import retrofit2.Response
import retrofit2.http.*

interface WallApiService {

    @GET("/api/{id}/wall/")
    suspend fun getAll(@Path("id") id: Long): Response<List<PostModel>>

    @GET("/api/{id}/wall/latest/")
    suspend fun getLatest(
        @Path("id") id: Int,
        @Query("count") count: Int
    ): Response<List<PostModel>>


    @GET("/api/{author_id}/wall/{post_id}/after/")
    suspend fun getAfter(
        @Path("author_id") authorId: Int,
        @Path("post_id") postId: Int,
        @Query("count") count: Int,
    ): Response<List<PostModel>>

    @GET("/api/{author_id}/wall/{post_id}/before/")
    suspend fun getBefore(
        @Path("author_id") authorId: Int,
        @Path("post_id") postId: Int,
        @Query("count") count: Int,
    ): Response<List<PostModel>>

}
