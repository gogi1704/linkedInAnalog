package com.example.linkedinanalog.data.repository.di

import com.example.linkedinanalog.data.repository.EventRepository
import com.example.linkedinanalog.data.repository.EventRepositoryImpl
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
    fun bindsPostRepository(impl: EventRepositoryImpl): EventRepository


}