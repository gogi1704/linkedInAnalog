package com.example.linkedinanalog.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.linkedinanalog.api.AuthApiService
import com.example.linkedinanalog.api.MediaApiService
import com.example.linkedinanalog.auth.AppAuth
import com.example.linkedinanalog.auth.AuthState
import com.example.linkedinanalog.data.models.Media
import com.example.linkedinanalog.data.models.MediaUpload
import com.example.linkedinanalog.data.models.user.UserModel
import com.example.linkedinanalog.data.models.user.UserRequestModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val mediaApiService: MediaApiService,
    private val auth: AppAuth,
    @ApplicationContext private val context: Context
) {
    val authData: LiveData<AuthState> = auth.authStateFlow
        .asLiveData(Dispatchers.Default)


    val isAuth: Boolean
        get() = auth.isAuth

    suspend fun registerUser(user: UserRequestModel): AuthState {
        val response = authApiService.registerUser(user.login, user.pass, user.name, null)
        if (response.isSuccessful) {
            return response.body()!!
        } else throw Exception()

    }

    suspend fun registerWithAvatar(user: UserRequestModel): AuthState {
        val mediaUpLoad = MediaUpload(user.avatar!!)
        val media = MultipartBody.Part.createFormData(
        "file", mediaUpLoad.file.name, mediaUpLoad.file.asRequestBody()
        )
        val response =
            authApiService.registerUser(user.login, user.pass, user.name, media)
        if (response.isSuccessful) {
            return response.body()!!
        } else throw Exception()
    }

    suspend fun uploadImage(upload: MediaUpload): Media {
        //todo
        val media = MultipartBody.Part.createFormData(
            "file", upload.file.name, upload.file.asRequestBody()
        )
        val response = mediaApiService.upLoadMedia(media)
        return response.body()!!
    }

    suspend fun authenticationUser(login: String, pass: String): AuthState {
        val response = authApiService.authenticationUser(login, pass)
        if (response.isSuccessful) {
            return response.body()!!
        } else return AuthState()

    }

     suspend fun getUserById(id: Long): UserModel {
        val response = authApiService.getUserById(id)
        if (response.isSuccessful) {
            return response.body()!!
        } else throw Exception()
    }

    suspend fun updateMyUser(): UserModel {
        val userId = context.getSharedPreferences("auth", Context.MODE_PRIVATE).getLong("id", 0)
        return if (userId != 0L) {
            getUserById(userId)
        } else UserModel(0, "", "", "")

    }

    fun signOut() {
        auth.removeAuth()
    }


}