package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.post.PostModel
import retrofit2.Response
import retrofit2.http.GET

interface PostApiService {

    @GET("/api/posts/")
   suspend fun getAllPosts():Response<List<PostModel>>
}