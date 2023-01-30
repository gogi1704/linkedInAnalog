package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.auth.AuthState
import com.example.linkedinanalog.data.models.user.UserModel
import com.example.linkedinanalog.data.models.user.UserRequestModel

interface AuthRepository {
    suspend fun registerUser(user: UserRequestModel): AuthState
    suspend fun authenticationUser(login: String, pass: String): AuthState
    suspend fun getAllUsers(): List<UserModel>
    suspend fun getUserById(id: Long): UserModel

}