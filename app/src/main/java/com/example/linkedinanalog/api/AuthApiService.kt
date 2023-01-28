package com.example.linkedinanalog.api

import com.example.linkedinanalog.auth.AuthState
import com.example.linkedinanalog.data.models.Media
import com.example.linkedinanalog.data.models.event.EventCreateRequest
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.models.user.UserModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface AuthApiService {

    @Multipart
    @POST("/api/users/registration/")
    suspend fun registerUser(
        @Part("login") login: String,
        @Part("password") password: String,
        @Part("name") name: String,
        @Part file: MultipartBody.Part?
    ): Response<AuthState>

    @FormUrlEncoded
    @POST("/api/users/authentication/")
    suspend fun authenticationUser(
        @Field("login") login: String,
        @Field("password") password: String,
    ): Response<AuthState>


    @GET("/api/users/{id}/")
    suspend fun getUserById(@Path("id") id: Long): Response<UserModel>

    @GET("/api/users/")
    suspend fun getAllUsers(): Response<List<UserModel>>
}