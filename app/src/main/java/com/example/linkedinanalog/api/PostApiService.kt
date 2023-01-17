package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.Media
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.models.post.PostModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostApiService {

    @GET("/api/posts/")
   suspend fun getAllPosts():Response<List<PostModel>>

   @POST("/api/posts/")
   suspend fun addPost(@Body post: PostCreateRequest):Response<PostModel>

   @Multipart
   @POST("/api/media/")
   suspend fun upLoadImage(@Part media: MultipartBody.Part ):Response<Media>

   @DELETE("/api/posts/{id}/")
   suspend fun removePost(@Path("id") id:Long):Response<Unit>
}