package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.Media
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.models.post.PostModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostApiService {

    @GET("/api/posts/")
    suspend fun getAllPosts(): Response<List<PostModel>>

    @GET("/api/posts/latest/")
    suspend fun getLatest(@Query("count") count: Int): Response<List<PostModel>>

    @GET("/api/posts/{id}/after/")
    suspend fun getAfter(@Path("id") id: Int , @Query("count") count: Int, ): Response<List<PostModel>>

    @GET("/api/posts/{id}/before/")
    suspend fun getBefore( @Path("id") id: Int , @Query("count") count: Int):Response<List<PostModel>>

    @POST("/api/posts/")
    suspend fun addPost(@Body post: PostCreateRequest): Response<PostModel>

    @POST("/api/posts/{id}/likes/")
    suspend fun likePost(@Path("id") id:Long):Response<PostModel>

    @DELETE("/api/posts/{id}/likes/")
    suspend fun dislikePost(@Path("id") id:Long):Response<PostModel>

    @DELETE("/api/posts/{id}/")
    suspend fun removePost(@Path("id") id: Long): Response<Unit>

    @GET("/api/posts/{id}/newer/")
    suspend fun getNewer(@Path("id") id: Long): Response<List<PostModel>>
}