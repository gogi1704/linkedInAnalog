package com.example.linkedinanalog.data.repository.di

import com.example.linkedinanalog.api.AuthApiService
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsEventRepository(impl: EventRepositoryImpl): Repository<EventModel>

    @Binds
    @Singleton
    fun bindsPostRepository(impl: PostRepositoryImpl): Repository<PostCreateRequest>

    @Binds
    @Singleton
    fun bindsJobRepository(impl: JobRepositoryImpl): Repository<JobModel>


}