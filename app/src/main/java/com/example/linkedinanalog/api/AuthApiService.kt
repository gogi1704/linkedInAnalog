package com.example.linkedinanalog.api

import com.example.linkedinanalog.auth.AuthState
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/users/registration/")
    suspend fun registerUser(
        @Field("login") login: String,
        @Field("password ") pass: String,
        @Field("name") name: String,
        @Field("file") file: String?
    ): Response<AuthState>

}