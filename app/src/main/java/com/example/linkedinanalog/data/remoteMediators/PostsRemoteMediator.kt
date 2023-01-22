package com.example.linkedinanalog.data.remoteMediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.linkedinanalog.api.PostApiService
import com.example.linkedinanalog.data.db.AppDb
import com.example.linkedinanalog.data.db.dao.postDao.PostDao
import com.example.linkedinanalog.data.db.dao.postDao.PostRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.postEntity.PostEntity
import com.example.linkedinanalog.data.db.entity.postEntity.PostRemoteKeyEntity
import com.example.linkedinanalog.data.db.entity.postEntity.toEntity

@OptIn(ExperimentalPagingApi::class)
class PostsRemoteMediator(
    private val apiService: PostApiService,
    private val db: AppDb,
    private val postDao: PostDao,
    private val keyDao: PostRemoteKeyDao,
) : RemoteMediator<Int, PostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {

        try {
            val response = when (loadType) {
                LoadType.REFRESH -> apiService.getLatest(state.config.initialLoadSize)
                LoadType.PREPEND -> {

                    val id = keyDao.max() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getAfter(id.toInt(), state.config.pageSize)
                }
                LoadType.APPEND -> {
                    val id = keyDao.min() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getBefore(id.toInt(), state.config.pageSize)
                }
            }
            if (!response.isSuccessful) {
                //TODO
                throw Exception()
            }
            val body = response.body() ?: throw Exception()
            db.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        keyDao.removeAll()
                        keyDao.insert(
                            listOf(
                                PostRemoteKeyEntity(
                                    type = PostRemoteKeyEntity.KeyType.AFTER,
                                    id = body.first().id.toLong(),
                                ),
                                PostRemoteKeyEntity(
                                    type = PostRemoteKeyEntity.KeyType.BEFORE,
                                    id = body.last().id.toLong(),
                                ),
                            )
                        )
                        postDao.removeAll()
                    }
                    LoadType.PREPEND -> {
                        keyDao.insert(
                            PostRemoteKeyEntity(
                                type = PostRemoteKeyEntity.KeyType.AFTER,
                                id = body.first().id.toLong()
                            )
                        )

                    }
                    LoadType.APPEND -> {
                        keyDao.insert(
                            PostRemoteKeyEntity(
                                type = PostRemoteKeyEntity.KeyType.BEFORE,
                                id = body.last().id.toLong()
                            )
                        )

                    }
                }


                postDao.insertPost(body.toEntity())
            }
            return MediatorResult.Success(endOfPaginationReached = body.isEmpty())
        } catch (e: Exception) {
            //TODO
            return MediatorResult.Error(e)
        }

    }
}