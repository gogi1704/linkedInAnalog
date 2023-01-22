package com.example.linkedinanalog.api

import com.example.linkedinanalog.auth.AuthState
import com.example.linkedinanalog.data.models.user.UserModel
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface AuthApiService {

    @FormUrlEncoded
    @POST("/api/users/registration/")
    suspend fun registerUser(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("file") file: File?
    ): Response<AuthState>

    @FormUrlEncoded
    @POST("/api/users/authentication/")
    suspend fun authenticationUser(
        @Field("login") login: String,
        @Field("password") password: String,
    ): Response<AuthState>



}