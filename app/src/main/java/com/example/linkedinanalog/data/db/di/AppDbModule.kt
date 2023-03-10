package com.example.linkedinanalog.data.db.di

import android.content.Context
import androidx.room.Room
import com.example.linkedinanalog.data.db.AppDb
import com.example.linkedinanalog.data.db.dao.eventDao.EventDao
import com.example.linkedinanalog.data.db.dao.eventDao.EventRemoteKeyDao
import com.example.linkedinanalog.data.db.dao.userDao.UserDao
import com.example.linkedinanalog.data.db.dao.postDao.PostDao
import com.example.linkedinanalog.data.db.dao.postDao.PostRemoteKeyDao
import com.example.linkedinanalog.data.db.dao.wallDao.WallDao
import com.example.linkedinanalog.data.db.dao.wallDao.WallRemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbModule {

    companion object {
        const val DATA_BASE_NAME = "APP_DATA_BASE"
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDb =
        Room.databaseBuilder(context, AppDb::class.java, DATA_BASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providePostDao(appDb: AppDb): PostDao = appDb.postDao()

    @Singleton
    @Provides
    fun providePostRemoteKeyDao(appDb: AppDb): PostRemoteKeyDao = appDb.postRemoteKeyDao()

    @Singleton
    @Provides
    fun provideWallDao(appDb: AppDb): WallDao = appDb.wallDao()

    @Singleton
    @Provides
    fun provideWallRemoteKeyDao(appDb: AppDb): WallRemoteKeyDao = appDb.wallRemoteKeyDao()

    @Singleton
    @Provides
    fun provideUserDao(appDb: AppDb): UserDao = appDb.userDao()

    @Singleton
    @Provides
    fun provideEventDao(appDb: AppDb): EventDao = appDb.eventDao()

    @Singleton
    @Provides
    fun provideEventRemoteKeyDao(appDb: AppDb): EventRemoteKeyDao = appDb.eventRemoteKeyDao()


}