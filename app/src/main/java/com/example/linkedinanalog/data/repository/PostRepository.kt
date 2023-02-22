package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.data.models.post.PostCreateRequest

interface PostRepository {
    suspend fun addItem(item: PostCreateRequest)
    suspend fun deleteItem(id: Long)
    suspend fun likeItem(id: Long, likeByMe: Boolean)
}