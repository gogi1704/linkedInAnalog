package com.example.linkedinanalog.data.db.di

import android.content.Context
import androidx.room.Room
import com.example.linkedinanalog.data.db.AppDb
import com.example.linkedinanalog.data.db.dao.PostDao
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
}