package com.example.linkedinanalog.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.linkedinanalog.api.AuthApiService
import com.example.linkedinanalog.auth.AppAuth
import com.example.linkedinanalog.auth.AuthState
import com.example.linkedinanalog.data.models.user.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import java.io.File
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val auth: AppAuth,
    @ApplicationContext private val context: Context
) {
    val authData: LiveData<AuthState> = auth.authStateFlow
        .asLiveData(Dispatchers.Default)


    val isAuth: Boolean
        get() = auth.isAuth

    suspend fun registerUser(login: String, pass: String, name: String, file: File?): AuthState {
        val response = authApiService.registerUser(login, pass, name, file)
        if (response.isSuccessful) {
            return response.body()!!
        } else return AuthState()
    }

    suspend fun authenticationUser(login: String, pass: String): AuthState {
        val response = authApiService.authenticationUser(login, pass)
        if (response.isSuccessful) {
            return response.body()!!
        } else return AuthState()

    }

    private suspend fun getUserById(id: Long): UserModel {
        val response = authApiService.getUserById(id)
        if (response.isSuccessful) {
            return response.body()!!
        } else throw Exception()
    }

    suspend fun updateMyUser(): UserModel {
        val userId = context.getSharedPreferences("auth", Context.MODE_PRIVATE).getLong("id", 0)
        return if (userId != 0L){
            getUserById(userId)
        } else UserModel(0 , "" , "" , "")

    }

    fun signOut() {
        auth.removeAuth()
    }


}