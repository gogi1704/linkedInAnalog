package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.data.models.post.PostModel

interface WallRepository {
    suspend fun likeItem(id: Long, likeByMe: Boolean)
    suspend fun removeAll()
}