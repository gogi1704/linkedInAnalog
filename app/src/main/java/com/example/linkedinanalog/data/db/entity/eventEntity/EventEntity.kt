package com.example.linkedinanalog.data.db.entity.eventEntity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.Coordinates
import com.example.linkedinanalog.data.models.EventType
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.models.user.UserModel
import com.google.gson.Gson

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val dateTime: String,
    val published: String,
    val coords: String?,
    val type: EventType,
    val likeOwnerIds: String?,
    val likedByMe: Boolean,
    val speakerIds: String?,
    val participantsIds: String?,
    val participatedByMe: Boolean,
    val attachment: String?,
    val link: String?,
    val ownedByMe: Boolean,
    val users: String
) {
    fun toDto(): EventModel {
        val gson = Gson()
        return EventModel(
            id = id,
            authorId = authorId,
            author = author,
            authorAvatar = authorAvatar,
            authorJob = authorJob,
            content = content,
            dateTime = dateTime,
            published = published,
            coords = gson.fromJson(coords, Coordinates::class.java),
            type = type,
            likeOwnerIds = if (likeOwnerIds?.isNotEmpty() == true) {
                gson.fromJson(
                    likeOwnerIds,
                    Array<Int>::class.java
                ).toList()
            } else null,
            likedByMe = likedByMe,
            speakerIds = if (speakerIds?.isNotEmpty() == true) {
                gson.fromJson(
                    speakerIds,
                    Array<Int>::class.java
                ).toList()
            } else null,
            participantsIds = if (participantsIds?.isNotEmpty() == true) {
                gson.fromJson(
                    participantsIds,
                    Array<Int>::class.java
                ).toList()
            } else null,
            participatedByMe,
            attachment = if (attachment != null) gson.fromJson(
                attachment,
                Attachment::class.java
            ) else null,
            link = link,
            ownedByMe = ownedByMe,
            users = gson.fromJson(users, UserModel::class.java)
        )
    }

    companion object {
        private val gson = Gson()
        fun fromDto(event: EventModel) = EventEntity(
            event.id,
            event.authorId,
            event.author,
            event.authorAvatar,
            event.authorJob,
            event.content,
            event.dateTime,
            event.published,
            gson.toJson(event.coords),
            event.type,
            gson.toJson(event.likeOwnerIds),
            event.likedByMe,
            gson.toJson(event.speakerIds),
            gson.toJson(event.participantsIds),
            event.participatedByMe,
            gson.toJson(event.attachment),
            event.link,
            event.ownedByMe,
            gson.toJson(event.users)
        )

    }

}

fun List<EventEntity>.toEventModel(): List<EventModel> = map { it.toDto() }
fun List<EventModel>.toEventEntity(): List<EventEntity> = map(EventEntity::fromDto)