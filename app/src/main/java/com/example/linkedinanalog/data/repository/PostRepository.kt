package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.data.models.post.PostCreateRequest
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun addItem(item: PostCreateRequest)
    suspend fun deleteItem(id: Long)
    fun getNewerItems(id: Long): Flow<Int>
    suspend fun likeItem(id: Long, likeByMe: Boolean)
}