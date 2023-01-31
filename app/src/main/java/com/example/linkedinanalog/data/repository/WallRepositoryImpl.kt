package com.example.linkedinanalog.data.repository

import android.app.Application
import android.database.SQLException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.linkedinanalog.api.PostApiService
import com.example.linkedinanalog.api.WallApiService
import com.example.linkedinanalog.data.db.AppDb
import com.example.linkedinanalog.data.db.dao.wallDao.WallDao
import com.example.linkedinanalog.data.db.dao.wallDao.WallRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.wallEntity.WallEntity
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.data.remoteMediators.WallRemoteMediator
import com.example.linkedinanalog.exceptions.ApiError
import com.example.linkedinanalog.exceptions.DbError
import com.example.linkedinanalog.exceptions.NetworkError
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class WallRepositoryImpl @Inject constructor(
    application: Application,
    wallApiService: WallApiService,
    private val postApiService: PostApiService,
    wallRemoteKeyDao: WallRemoteKeyDao,
    db: AppDb,
    private val wallDao: WallDao
) : WallRepository {

    @OptIn(ExperimentalPagingApi::class)
    val pagingData: Flow<PagingData<WallEntity>> = Pager(
        config = PagingConfig(10),
        remoteMediator = WallRemoteMediator(
            application,
            db,
            wallDao,
            wallRemoteKeyDao,
            wallApiService
        ),
        pagingSourceFactory = wallDao::pagingSource
    )
        .flow

    override suspend fun removeAll() {
        try {
            wallDao.removeAll()
        } catch (sql: SQLException) {
            throw DbError()
        } catch (e: Exception) {
            throw UnknownError()
        }
    }


    override suspend fun likeItem(id: Long, likeByMe: Boolean) {
        try {
            if (likeByMe) {
                val response = postApiService.dislikePost(id)
                if (response.isSuccessful) {
                    val body =
                        response.body() ?: throw ApiError(response.code(), response.message())
                    wallDao.insertWall(
                        WallEntity.fromDto(
                            body.copy(likedByMe = false)
                        )
                    )
                } else throw ApiError(response.code(), response.message())
            } else {
                val response = postApiService.likePost(id)
                if (response.isSuccessful) {
                    val body =
                        response.body() ?: throw ApiError(response.code(), response.message())
                    wallDao.insertWall(WallEntity.fromDto(body.copy(likedByMe = true)))
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