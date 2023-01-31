package com.example.linkedinanalog.data.repository


import android.database.SQLException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.linkedinanalog.api.EventApiService
import com.example.linkedinanalog.api.MediaApiService
import com.example.linkedinanalog.data.db.AppDb
import com.example.linkedinanalog.data.db.dao.eventDao.EventDao
import com.example.linkedinanalog.data.db.dao.eventDao.EventRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.eventEntity.EventEntity
import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.AttachmentType
import com.example.linkedinanalog.data.models.Media
import com.example.linkedinanalog.data.models.MediaUpload
import com.example.linkedinanalog.data.models.event.EventCreateRequest
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.remoteMediators.EventRemoteMediator
import com.example.linkedinanalog.exceptions.ApiError
import com.example.linkedinanalog.exceptions.DbError
import com.example.linkedinanalog.exceptions.NetworkError
import com.example.linkedinanalog.exceptions.UnknownError
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.IOException
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    db: AppDb,
    keyDao: EventRemoteKeyDao,
    private val eventDao: EventDao,
    private val eventApiService: EventApiService,
    private val mediaApiService: MediaApiService
) :
    EventRepository {


    @OptIn(ExperimentalPagingApi::class)
    val pagingData: Flow<PagingData<EventEntity>> = Pager(
        config = PagingConfig(7),
        remoteMediator = EventRemoteMediator(db, eventApiService, eventDao, keyDao),
        pagingSourceFactory = eventDao::getPagingSource
    ).flow


    private val chooseList = mutableListOf<Int>()


    fun addChooseUser(userId: Int) {
        if (chooseList.contains(userId)) {
            chooseList.remove(userId)
        } else
            chooseList.add(userId)
    }


    override suspend fun createEvent(item: EventCreateRequest) {
        try {
            val response = eventApiService.createEvent(item.copy(speakerIds = chooseList))
            if (response.isSuccessful) {
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                eventDao.insertEvent(EventEntity.fromDto(body))
                chooseList.clear()
            } else throw ApiError(response.code(), response.message())
        } catch (io: IOException) {
            throw NetworkError()
        } catch (sql: SQLException) {
            throw DbError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }


    suspend fun createWithAttachments(event: EventCreateRequest, mediaUpload: MediaUpload?) {
        val media = uploadImage(mediaUpload!!)
        val eventCopy = event.copy(attachment = Attachment(media.url, AttachmentType.IMAGE))
        createEvent(eventCopy)

    }

    private suspend fun uploadImage(upload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody()
            )
            val response = mediaApiService.upLoadMedia(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (io: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }

    }

    override suspend fun participantByMe(id: Long, isParticipatedByMe: Boolean) {
        try {
            val response = if (isParticipatedByMe) {
                eventApiService.participantByMeFalse(id)
            } else {
                eventApiService.participantByMeTrue(id)
            }

            if (response.isSuccessful) {
                eventDao.insertEvent(
                    EventEntity.fromDto(
                        response.body() ?: throw ApiError(response.code(), response.message())
                    )
                )
            } else throw ApiError(response.code(), response.message())

        } catch (io: IOException) {
            throw NetworkError()
        } catch (sql: SQLException) {
            throw DbError()
        } catch (e: Exception) {
            throw UnknownError()
        }


    }

    override suspend fun deleteEvent(id: Long) {
        try {
            val response = eventApiService.deleteEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            } else {
                eventDao.deleteEvent(id.toInt())
            }
        } catch (io: IOException) {
            throw NetworkError()
        } catch (sql: SQLException) {
            throw DbError()
        } catch (e: Exception) {
            throw UnknownError()
        }

    }


}
