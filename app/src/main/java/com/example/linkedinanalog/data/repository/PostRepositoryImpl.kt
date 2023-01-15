package com.example.linkedinanalog.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.linkedinanalog.api.PostApiService
import com.example.linkedinanalog.data.db.dao.PostDao
import com.example.linkedinanalog.data.db.entity.PostEntity
import com.example.linkedinanalog.data.db.entity.toEntity
import com.example.linkedinanalog.data.models.post.PostModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*


class PostRepositoryImpl @Inject constructor(
    private val apiService: PostApiService,
    private val postDao: PostDao
) : Repository {

    val pagingData: Flow<PagingData<PostEntity>> = Pager(
        PagingConfig(5, enablePlaceholders = false)
    ) {
        postDao.getPagingData()
    }.flow

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
            postDao.insertPost(data.toEntity())

        } else throw Exception()
    }
}