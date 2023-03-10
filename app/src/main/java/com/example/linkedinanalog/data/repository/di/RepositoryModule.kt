package com.example.linkedinanalog.data.repository.di

import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.repository.*
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
    fun bindsEventRepository(impl: EventRepositoryImpl): EventRepository


    @Binds
    @Singleton
    fun bindsJobRepository(impl: JobRepositoryImpl): JobRepository

    @Binds
    @Singleton
    fun bindsPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    fun bindsAuthRepository(impl:AuthRepositoryImpl):AuthRepository

    @Binds
    @Singleton
    fun bindsWallRepository(impl:WallRepositoryImpl):WallRepository




}