package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.api.JobApiService
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.exceptions.ApiError
import com.example.linkedinanalog.exceptions.NetworkError
import java.io.IOException
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(private val jobApiService: JobApiService) :
    JobRepository {


    override suspend fun getAll(): List<JobModel> {
        try {
            val response = jobApiService.getAllMyJobs()
            if (response.isSuccessful) {
                return response.body() ?: throw ApiError(response.code(), response.message())
            } else throw ApiError(response.code(), response.message())
        } catch (io: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw com.example.linkedinanalog.exceptions.UnknownError()
        }

    }

    override suspend fun addItem(item: JobModel) {
        try {
            val response = jobApiService.addJob(item)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (io: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw com.example.linkedinanalog.exceptions.UnknownError()
        }
    }

    override suspend fun getJobById(id: Long): List<JobModel> {
        try {
            val response = jobApiService.getJobById(id)
            if (response.isSuccessful) {
                return response.body() ?: throw ApiError(response.code(), response.message())
            } else throw ApiError(response.code(), response.message())
        } catch (io: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw com.example.linkedinanalog.exceptions.UnknownError()
        }

    }

    override suspend fun deleteJob(id: Long) {
        try {
            val response = jobApiService.deleteJob(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (io: IOException) {
            throw NetworkError()
        } catch (e: Exception) {
            throw com.example.linkedinanalog.exceptions.UnknownError()
        }
    }
}