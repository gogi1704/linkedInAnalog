package com.example.linkedinanalog.data.models.post

import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.Coordinates
import com.example.linkedinanalog.data.models.user.UserModel

data class PostModel(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    val coords: Coordinates?,
    val link: String?,
    val likeOwnerIds: List<Int>? = emptyList(),
    val mentionsIds: List<Int>? = emptyList(),
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment?,
    val ownedByMe: Boolean,
    val users: UserModel

){
    fun toPostCreateRequest():PostCreateRequest = PostCreateRequest(id , content , coords , link , attachment , mentionsIds)
}
