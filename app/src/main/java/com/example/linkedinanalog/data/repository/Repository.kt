package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.data.models.post.PostModel

interface Repository<T> {
   suspend fun getAll()
   suspend fun addItem(item: T)
   suspend fun deleteItem(id:Long)
}

