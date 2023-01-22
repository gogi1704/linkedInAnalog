package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.Media
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MediaApiService {

    @Multipart
    @POST("/api/media/")
    suspend fun upLoadMedia(@Part media: MultipartBody.Part): Response<Media>
}