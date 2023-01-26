package com.example.linkedinanalog.data.repository

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.linkedinanalog.api.WallApiService
import com.example.linkedinanalog.data.db.AppDb
import com.example.linkedinanalog.data.db.dao.wallDao.WallDao
import com.example.linkedinanalog.data.db.dao.wallDao.WallRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.postEntity.PostEntity
import com.example.linkedinanalog.data.db.entity.wallEntity.WallEntity
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.data.remoteMediators.WallRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WallRepository @Inject constructor(
    application: Application,
    private val wallApiService: WallApiService,
    wallRemoteKeyDao: WallRemoteKeyDao,
    db: AppDb,
    private val wallDao: WallDao
) {

    @OptIn(ExperimentalPagingApi::class)
    val pagingData: Flow<PagingData<WallEntity>> = Pager(
        config = PagingConfig(6),
        remoteMediator = WallRemoteMediator(
            application,
            db,
            wallDao,
            wallRemoteKeyDao,
            wallApiService
        ),
        pagingSourceFactory = wallDao::pagingSource)
    .flow

    suspend fun removeAll(){
        wallDao.removeAll()
    }



    suspend fun getAll(id: Long): List<PostModel> {
        val response = wallApiService.getAll(id)
        if (response.isSuccessful) {
            return response.body() ?: listOf()
        } else throw Exception()

    }

    suspend fun likeItem(id: Long, likeByMe: Boolean) {
        if (likeByMe) {
            val response = wallApiService.dislikePost(id)
            if (response.isSuccessful) {
                wallDao.insertWall(WallEntity.fromDto(response.body()!!.copy(likedByMe = false)))
            }
        } else {
            val response = wallApiService.likePost(id)
            if (response.isSuccessful) {
                wallDao.insertWall(WallEntity.fromDto(response.body()!!.copy(likedByMe = true)))
            }
        }
    }
}