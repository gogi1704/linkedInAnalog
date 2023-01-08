package com.example.linkedinanalog.data.models.event

import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.Coordinates
import com.example.linkedinanalog.data.models.user.UsersPreview

data class EventModel(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val dateTime: String,
    val published: String,
    val coords: Coordinates?,
    val type: String,
    val likeOwnerIds: List<Int>?,
    val likedByMe: Boolean,
    val speakerIds: List<Int>?,
    val participantsIds: List<Int>?,
    val participatedByMe: Boolean,
    val attachment: Attachment?,
    val link: String?,
    val ownedByMe:Boolean,
    val users:List<UsersPreview>
    )
