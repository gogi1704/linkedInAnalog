package com.example.linkedinanalog.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.linkedinanalog.api.PostApiService
import com.example.linkedinanalog.data.models.post.PostModel
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(private val apiService: PostApiService) : Repository {

   private var data = listOf<PostModel>()
        set(value) {
            field = value
            liveData.value = value
        }
    val liveData = MutableLiveData(data)
    override suspend fun getAll() {
        val response = apiService.getAllPosts()
        if (response.isSuccessful) {
            val body = response.body()
            data = body ?: listOf()
        } else throw Exception()
    }
}