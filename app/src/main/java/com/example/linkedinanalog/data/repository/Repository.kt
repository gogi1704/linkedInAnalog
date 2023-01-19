package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.data.models.post.PostModel
import kotlinx.coroutines.flow.Flow

interface Repository<T> {
   suspend fun getAll()
   suspend fun addItem(item: T)
   suspend fun deleteItem(id:Long)
    fun getNewerItems(id: Long):Flow<Int>
}

