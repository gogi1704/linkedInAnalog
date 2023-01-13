package com.example.linkedinanalog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.linkedinanalog.api.AuthApiService
import com.example.linkedinanalog.auth.AppAuth
import com.example.linkedinanalog.auth.AuthState
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import java.io.File
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val auth: AppAuth
) {
    val authData:LiveData<AuthState> = auth.authStateFlow
        .asLiveData(Dispatchers.Default)

    suspend fun registerUser(login: String, pass: String, name: String, file: File?): AuthState {
        val response = authApiService.registerUser(login, pass, name, file)
        if (response.isSuccessful) {
            return response.body()!!
        } else return AuthState()
    }

    suspend fun authenticationUser(login: String, pass: String):AuthState{
        val response = authApiService.authenticationUser(login , pass)
        if (response.isSuccessful){
            return response.body()!!
        }else return AuthState()

    }

    fun signOut(){
        auth.removeAuth()
    }

val isAuth:Boolean
get() = auth.isAuth

}