package com.example.linkedinanalog.data.repository.di

import com.example.linkedinanalog.data.repository.Repository
import com.example.linkedinanalog.data.repository.EventRepositoryImpl
import com.example.linkedinanalog.data.repository.JobRepositoryImpl
import com.example.linkedinanalog.data.repository.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsEventRepository(impl: EventRepositoryImpl): Repository

    @Binds
    @Singleton
    fun bindsPostRepository(impl: PostRepositoryImpl): Repository

    @Binds
    @Singleton
    fun bindsJobRepository(impl: JobRepositoryImpl): Repository


}