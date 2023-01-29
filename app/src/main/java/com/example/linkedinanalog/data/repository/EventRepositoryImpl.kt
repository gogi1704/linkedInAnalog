package com.example.linkedinanalog.data.repository


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
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val db: AppDb,
    private val keyDao: EventRemoteKeyDao,
    private val eventDao: EventDao,
    private val eventApiService: EventApiService,
    private val mediaApiService: MediaApiService
) :
    Repository<EventModel> {


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

    override suspend fun getAll(): List<EventModel> {
//        val response = eventApiService.getAllEvents()
//        if (response.isSuccessful) {
//            return response.body()!!
//        } else throw Exception()
        return listOf()
    }


    suspend fun createEvent(item: EventCreateRequest) {
        //todo
        val response = eventApiService.createEvent(item.copy(speakerIds = chooseList))
        if (response.isSuccessful) {
            response.body()
            chooseList.clear()
        } else throw Exception()
    }

    suspend fun createWithAttachments(event: EventCreateRequest, mediaUpload: MediaUpload?) {
        val media = uploadImage(mediaUpload!!)
        val eventCopy = event.copy(attachment = Attachment(media.url, AttachmentType.IMAGE))
        createEvent(eventCopy)

    }

    suspend fun uploadImage(upload: MediaUpload): Media {
        //todo
        val media = MultipartBody.Part.createFormData(
            "file", upload.file.name, upload.file.asRequestBody()
        )
        val response = mediaApiService.upLoadMedia(media)
        return response.body()!!
    }

    suspend fun participantByMe(id: Long, isParticipatedByMe: Boolean) {
        val response = if (isParticipatedByMe) {
            eventApiService.participantByMeFalse(id)
        } else {
            eventApiService.participantByMeTrue(id)
        }
        if (response.isSuccessful) {
            eventDao.insertEvent(EventEntity.fromDto(response.body() ?: throw Exception()))
        } else throw Exception()

    }


    override suspend fun addItem(item: EventModel) {
//todo
    }

    override suspend fun deleteItem(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getNewerItems(id: Long): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun likeItem(id: Long, likeByMe: Boolean) {
        TODO("Not yet implemented")
    }
}
