package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.api.AuthApiService
import com.example.linkedinanalog.auth.AuthState
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApiService: AuthApiService) {

    suspend fun registerUser(login: String, pass: String, name: String, file: String?): AuthState {

        val response = authApiService.registerUser(login, pass, name, file)
        if (response.isSuccessful) {
            return response.body()!!
        } else return AuthState()
    }
}