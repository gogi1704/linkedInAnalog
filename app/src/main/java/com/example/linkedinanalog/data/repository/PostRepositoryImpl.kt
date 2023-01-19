package com.example.linkedinanalog.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.linkedinanalog.api.PostApiService
import com.example.linkedinanalog.data.db.AppDb
import com.example.linkedinanalog.data.db.dao.PostDao
import com.example.linkedinanalog.data.db.dao.PostRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.PostEntity
import com.example.linkedinanalog.data.db.entity.toEntity
import com.example.linkedinanalog.data.models.*
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.data.remoteMediators.PostsRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

//TODO create posts newerCount and swipeRefresh


class PostRepositoryImpl @Inject constructor(
    private val apiService: PostApiService,
    db: AppDb,
    keyDao: PostRemoteKeyDao,
    private val postDao: PostDao
) : Repository<PostCreateRequest> {

    @OptIn(ExperimentalPagingApi::class)
    val pagingData: Flow<PagingData<PostEntity>> = Pager(
       config =  PagingConfig(3),
        remoteMediator = PostsRemoteMediator(apiService , db , postDao , keyDao ),
        pagingSourceFactory = postDao::pagingSource
    ).flow


    private var data = listOf<PostModel>()
        set(value) {
            field = value
            liveData.value = value
        }
    val liveData = MutableLiveData(data)


    override suspend fun getAll() {
        //todo
        val response = apiService.getAllPosts()
        if (response.isSuccessful) {
            val body = response.body()
            data = body ?: listOf()
            postDao.insertPost(data.toEntity())

        } //else throw Exception()
    }

    override suspend fun addItem(item: PostCreateRequest) {
        //todo
        val response = apiService.addPost(item)
        if (response.isSuccessful) {
            val body = response.body()
            postDao.insertPost(PostEntity.fromDto(body!!))
        } else {
            response
        }
    }


    suspend fun addItemWithAttachments(postModel: PostCreateRequest, mediaUpload: MediaUpload) {
        val media = uploadImage(mediaUpload)
        val post = postModel.copy(attachment = Attachment(media.url, AttachmentType.IMAGE))
        addItem(post)

    }

    suspend fun uploadImage(upload: MediaUpload): Media {
        //todo
        val media = MultipartBody.Part.createFormData(
            "file", upload.file.name, upload.file.asRequestBody()
        )
        val response = apiService.upLoadImage(media)
        return response.body()!!
    }

    override suspend fun deleteItem(id: Long) {
        //TODO
        val response = apiService.removePost(id)
        if (response.isSuccessful) {
            postDao.deletePost(id)
        } else {
            response
        }
    }

    override fun getNewerItems(id: Long): Flow<Int> = flow {
        while (true) {
            delay(10_000)
            val response = apiService.getNewer(id)
            if (!response.isSuccessful) {
                throw Exception()
            }
                val body = response.body() ?: throw Exception()
                postDao.insertPost(body.toEntity())
                emit(body.size)


        }
    }
        .catch { }
        .flowOn(Dispatchers.Default)


}