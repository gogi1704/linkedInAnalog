package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.user.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {

    @GET("/api/users/{id}/")
    suspend fun getUserById(@Path("id") id: Long): Response<UserModel>
}