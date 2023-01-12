package com.example.linkedinanalog.api.di

import com.example.linkedinanalog.BuildConfig
import com.example.linkedinanalog.api.AuthApiService
import com.example.linkedinanalog.api.EventApiService
import com.example.linkedinanalog.api.JobApiService
import com.example.linkedinanalog.api.PostApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    companion object {
        private const val BASE_URL = "https://netomedia.ru/"
    }

    @Singleton
    @Provides
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideOkHTTP(logging: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideEventApiService(retrofit: Retrofit): EventApiService = retrofit.create()


    @Singleton
    @Provides
    fun providePostApiService(retrofit: Retrofit): PostApiService = retrofit.create()


    @Singleton
    @Provides
    fun providesJobApiService(retrofit: Retrofit): JobApiService = retrofit.create()


    @Singleton
    @Provides
    fun providesAuthApiService(retrofit: Retrofit): AuthApiService = retrofit.create()


}