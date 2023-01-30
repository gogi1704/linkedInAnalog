package com.example.linkedinanalog.data.repository

import android.content.Context
import android.database.SQLException
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.linkedinanalog.api.AuthApiService
import com.example.linkedinanalog.auth.AppAuth
import com.example.linkedinanalog.auth.AuthState
import com.example.linkedinanalog.data.db.dao.userDao.UserDao
import com.example.linkedinanalog.data.models.MediaUpload
import com.example.linkedinanalog.data.models.user.UserModel
import com.example.linkedinanalog.data.models.user.UserRequestModel
import com.example.linkedinanalog.exceptions.ApiError
import com.example.linkedinanalog.exceptions.DbError
import com.example.linkedinanalog.exceptions.NetworkError
import com.example.linkedinanalog.exceptions.UnknownError
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userDao: UserDao,
    private val auth: AppAuth,
    @ApplicationContext private val context: Context
) : AuthRepository {
    val authData: LiveData<AuthState> = auth.authStateFlow
        .asLiveData(Dispatchers.Default)


    val isAuth: Boolean
        get() = auth.isAuth



    override suspend fun registerUser(user: UserRequestModel): AuthState {
        try {
            val response = authApiService.registerUser(user.login, user.pass, user.name, null)
            if (response.isSuccessful) {
                return response.body() ?: throw ApiError(response.code(), response.message())
            } else throw Exception()
        } catch (io: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }


    suspend fun registerWithAvatar(user: UserRequestModel): AuthState {
        try {
            val mediaUpLoad = MediaUpload(user.avatar!!)
            val media = MultipartBody.Part.createFormData(
                "file", mediaUpLoad.file.name, mediaUpLoad.file.asRequestBody()
            )
            val response =
                authApiService.registerUser(user.login, user.pass, user.name, media)
            if (response.isSuccessful) {
                return response.body() ?: throw ApiError(response.code(), response.message())
            } else throw ApiError(response.code(), response.message())
        } catch (io: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }


    override suspend fun authenticationUser(login: String, pass: String): AuthState {
        try {
            val response = authApiService.authenticationUser(login, pass)
            if (response.isSuccessful) {
                return response.body() ?: throw ApiError(response.code(), response.message())
            } else throw ApiError(response.code(), response.message())
        } catch (io: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }


    override suspend fun getAllUsers(): List<UserModel> {
        try {
            val response = authApiService.getAllUsers()
            if (response.isSuccessful) {
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                userDao.insert(body.map { it.toEntity() })
                return body
            } else throw throw ApiError(response.code(), response.message())
        } catch (io: IOException) {
            throw NetworkError()
        }catch (sql: SQLException) {
            throw DbError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }


    override suspend fun getUserById(id: Long): UserModel {
        try {
            val response = authApiService.getUserById(id)
            if (response.isSuccessful) {
                return response.body() ?: throw ApiError(response.code(), response.message())
            } else throw  ApiError(response.code(), response.message())
        } catch (io: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
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