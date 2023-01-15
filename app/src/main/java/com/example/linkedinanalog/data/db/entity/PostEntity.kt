package com.example.linkedinanalog.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.Coordinates
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.data.models.user.UserModel
import com.google.gson.Gson


@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    val coords: String?, ///
    val link: String?,
    val likeOwnerIds: String?,//
    val mentionsIds: String?,//
    val mentionedMe: Boolean,
    val likeByMe: Boolean,
    val attachment: String?,//
    val ownedByMe: Boolean,
    val users: String//
) {

    fun toDto(): PostModel {
        val gson = Gson()
        return PostModel(
            id = id,
            authorId = authorId,
            author = author,
            authorAvatar = authorAvatar,
            authorJob = authorJob,
            content = content,
            published = published,
            coords = gson.fromJson(coords, Coordinates::class.java),
            link = link,
            likeOwnerIds = if (likeOwnerIds?.length!! <= 0) {
                gson.fromJson(
                    likeOwnerIds,
                    Array<Int>::class.java
                ).toList()
            } else null,
            mentionsIds = if (mentionsIds?.length!! <= 0) {
                gson.fromJson(
                    mentionsIds,
                    Array<Int>::class.java
                ).toList()
            } else null,
            mentionedMe = mentionedMe,
            likeByMe = likeByMe,
            attachment = if (attachment?.length!! <= 0) gson.fromJson(
                attachment,
                Attachment::class.java
            ) else null,
            ownedByMe = ownedByMe,
            users = gson.fromJson(users, UserModel::class.java)
        )
    }

    companion object {
        private val gson = Gson()
        fun fromDto(post: PostModel) = PostEntity(
            post.id,
            post.authorId,
            post.author,
            post.authorAvatar,
            post.authorJob,
            post.content,
            post.published,
            gson.toJson(post.coords),
            post.link,
            gson.toJson(post.likeOwnerIds),
            gson.toJson(post.mentionsIds),
            post.mentionedMe,
            post.likeByMe,
            gson.toJson(post.attachment),
            post.ownedByMe,
            gson.toJson(post.users)

        )

    }

}

fun List<PostEntity>.toDto(): List<PostModel> = map { it.toDto() }
fun List<PostModel>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
