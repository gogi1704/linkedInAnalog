package com.example.linkedinanalog.data.remoteMediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.linkedinanalog.api.EventApiService
import com.example.linkedinanalog.data.db.AppDb
import com.example.linkedinanalog.data.db.dao.eventDao.EventDao
import com.example.linkedinanalog.data.db.dao.eventDao.EventRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.eventEntity.EventEntity
import com.example.linkedinanalog.data.db.entity.eventEntity.EventRemoteKeyEntity
import com.example.linkedinanalog.data.db.entity.eventEntity.toEventEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EventRemoteMediator @Inject constructor(
    private val db: AppDb,
    private val eventApiService: EventApiService,
    private val eventDao: EventDao,
    private val keyDao: EventRemoteKeyDao

) : RemoteMediator<Int, EventEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventEntity>
    ): MediatorResult {

        try {
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    eventApiService.getLatest(state.config.initialLoadSize)
                }
                LoadType.PREPEND -> {
                    val id = keyDao.max() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    eventApiService.getAfter(id, state.config.pageSize)
                }

                LoadType.APPEND -> {

                    val id = keyDao.min() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    eventApiService.getBefore(id, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) {
                throw Exception()
            }
            val body = response.body() ?: throw Exception()
            db.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        keyDao.removeAll()
                        keyDao.insert(
                            listOf(
                                EventRemoteKeyEntity(
                                    type = EventRemoteKeyEntity.KeyType.AFTER,
                                    id = body.first().id.toLong(),
                                ),
                                EventRemoteKeyEntity(
                                    type = EventRemoteKeyEntity.KeyType.BEFORE,
                                    id = body.last().id.toLong(),
                                ),
                            )
                        )
                        eventDao.removeAll()
                    }
                    LoadType.PREPEND -> {
                        keyDao.insert(
                            EventRemoteKeyEntity(
                                type = EventRemoteKeyEntity.KeyType.AFTER,
                                id = body.first().id.toLong()
                            )
                        )

                    }
                    LoadType.APPEND -> {
                        keyDao.insert(
                            EventRemoteKeyEntity(
                                type = EventRemoteKeyEntity.KeyType.BEFORE,
                                id = body.last().id.toLong()
                            )
                        )

                    }
                }

                eventDao.insertEvent(body.toEventEntity())
            }
            return MediatorResult.Success(endOfPaginationReached = body.isEmpty())
        } catch (e: Exception) {
            println(e)
            return MediatorResult.Error(e)
        }

    }
}