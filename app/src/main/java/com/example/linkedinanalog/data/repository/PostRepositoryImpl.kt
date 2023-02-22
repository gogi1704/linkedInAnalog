package com.example.linkedinanalog.data.repository

import android.database.SQLException
import androidx.paging.*
import com.example.linkedinanalog.api.MediaApiService
import com.example.linkedinanalog.api.PostApiService
import com.example.linkedinanalog.data.db.AppDb
import com.example.linkedinanalog.data.db.dao.postDao.PostDao
import com.example.linkedinanalog.data.db.dao.postDao.PostRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.postEntity.PostEntity
import com.example.linkedinanalog.data.models.*
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.remoteMediators.PostsRemoteMediator
import com.example.linkedinanalog.exceptions.*
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.IOException


class PostRepositoryImpl @Inject constructor(
    private val apiService: PostApiService,
    private val mediaApiService: MediaApiService,
    db: AppDb,
    keyDao: PostRemoteKeyDao,
    private val postDao: PostDao
) : PostRepository {


    @OptIn(ExperimentalPagingApi::class)
    val pagingData: Flow<PagingData<PostEntity>> = Pager(
        config = PagingConfig(6),
        remoteMediator = PostsRemoteMediator(apiService, db, postDao, keyDao),
        pagingSourceFactory = postDao::pagingSource
    ).flow




    override suspend fun addItem(item: PostCreateRequest) {
        try {
            val response = apiService.addPost(item)
            if (response.isSuccessful) {
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                postDao.insertPost(PostEntity.fromDto(body))
            } else {
                throw ApiError(response.code(), response.message())
            }
        } catch (io: IOException) {
            throw NetworkError()
        } catch (sql: SQLException) {
            throw DbError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }


    suspend fun addItemWithAttachments(postModel: PostCreateRequest, mediaUpload: MediaUpload) {
        val media = uploadImage(mediaUpload)
        val post = postModel.copy(attachment = Attachment(media.url, AttachmentType.IMAGE))
        addItem(post)

    }

    private suspend fun uploadImage(upload: MediaUpload): Media {
        val media = MultipartBody.Part.createFormData(
            "file", upload.file.name, upload.file.asRequestBody()
        )
        try {
            val response = mediaApiService.upLoadMedia(media)
            if (response.isSuccessful) {
                return response.body() ?: throw ApiError(response.code(), response.message())
            } else {
                throw ApiError(response.code(), response.message())
            }
        } catch (io: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }

    override suspend fun deleteItem(id: Long) {
        try {
            val response = apiService.removePost(id)
            if (response.isSuccessful) {
                postDao.deletePost(id)
            } else {
                throw ApiError(response.code(), response.message())
            }
        } catch (io: IOException) {
            throw NetworkError()
        } catch (sql: SQLException) {
            throw DbError()
        } catch (e: Exception) {
            throw UnknownError()
        }

    }


    override suspend fun likeItem(id: Long, likeByMe: Boolean) {
        try {
            if (likeByMe) {
                val response = apiService.dislikePost(id)
                if (response.isSuccessful) {
                    val body =
                        response.body() ?: throw ApiError(response.code(), response.message())
                    postDao.insertPost(
                        PostEntity.fromDto(
                            body.copy(likedByMe = false)
                        )
                    )
                } else throw ApiError(response.code(), response.message())
            } else {
                val response = apiService.likePost(id)
                if (response.isSuccessful) {
                    val body =
                        response.body() ?: throw ApiError(response.code(), response.message())
                    postDao.insertPost(
                        PostEntity.fromDto(
                            body.copy(likedByMe = true)
                        )
                    )
                } else throw ApiError(response.code(), response.message())
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