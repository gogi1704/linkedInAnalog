package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.post.PostModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface PostApiService {

    @GET("/api/posts/")
   suspend fun getAllPosts():Response<List<PostModel>>

   @POST("/api/posts/")
   suspend fun addPost():Response<PostModel>
}