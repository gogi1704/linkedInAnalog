package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.data.models.post.PostModel
import kotlinx.coroutines.flow.Flow

interface Repository<T> {
    suspend fun getAll():List<T>
    suspend fun addItem(item: T)
    suspend fun deleteItem(id: Long)
    suspend fun likeItem(id: Long, likeByMe: Boolean)
    fun getNewerItems(id: Long): Flow<Int>
}

