package com.example.linkedinanalog.data.remoteMediators

import android.app.Application
import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.linkedinanalog.api.WallApiService
import com.example.linkedinanalog.data.db.AppDb
import com.example.linkedinanalog.data.db.dao.wallDao.WallDao
import com.example.linkedinanalog.data.db.dao.wallDao.WallRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.postEntity.toPostEntity
import com.example.linkedinanalog.data.db.entity.wallEntity.WallEntity
import com.example.linkedinanalog.data.db.entity.wallEntity.WallRemoteKeyEntity
import com.example.linkedinanalog.data.db.entity.wallEntity.toWallEntity
import com.example.linkedinanalog.exceptions.ApiError
import com.example.linkedinanalog.ui.constans.SHOW_USER_KEY
import com.example.linkedinanalog.ui.constans.USER_ID_PREFS
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class WallRemoteMediator @Inject constructor(
    private val application: Application,
    private val db: AppDb,
    private val wallDao: WallDao,
    private val wallRemoteKeyDao: WallRemoteKeyDao,
    private val wallApiService: WallApiService,
) : RemoteMediator<Int, WallEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, WallEntity>
    ): MediatorResult {
        try {

            val userId = application.applicationContext.getSharedPreferences(
                USER_ID_PREFS,
                Context.MODE_PRIVATE
            )
                .getLong(SHOW_USER_KEY, 0).toInt()
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    wallApiService.getLatest(userId, state.config.pageSize)
                }

                LoadType.PREPEND -> {
                    val id = wallRemoteKeyDao.max() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    if (wallApiService.getAfter(userId, id.toInt(), state.config.pageSize).body()
                            ?.last()?.id?.toLong() == id
                    ) {
                        null
                    } else
                        wallApiService.getAfter(userId, id.toInt(), state.config.pageSize)
                }
                LoadType.APPEND -> {
                    val id = wallRemoteKeyDao.min() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    wallApiService.getBefore(userId, id.toInt(), state.config.pageSize)
                }
            }

            if (!response?.isSuccessful!!) {
                throw ApiError(response.code() , response.message())
            }
            val body = response.body() ?: throw Exception()
            db.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        wallRemoteKeyDao.removeAll()
                        wallRemoteKeyDao.insert(
                            listOf(
                                WallRemoteKeyEntity(
                                    type = WallRemoteKeyEntity.KeyType.AFTER,
                                    id = body.first().id.toLong(),
                                ),
                                WallRemoteKeyEntity(
                                    type = WallRemoteKeyEntity.KeyType.BEFORE,
                                    id = body.last().id.toLong(),
                                ),
                            )
                        )
                        wallDao.removeAll()
                    }
                    LoadType.PREPEND -> {
                        wallRemoteKeyDao.insert(
                            WallRemoteKeyEntity(
                                type = WallRemoteKeyEntity.KeyType.BEFORE,
                                id = body.last().id.toLong()
                            )
                        )

                    }
                    LoadType.APPEND -> {
                        wallRemoteKeyDao.insert(
                            WallRemoteKeyEntity(
                                type = WallRemoteKeyEntity.KeyType.AFTER,
                                id = body.first().id.toLong()
                            )
                        )

                    }
                }


                wallDao.insertWall(body.toWallEntity())
            }
            return MediatorResult.Success(endOfPaginationReached = body.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }


}