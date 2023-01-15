package com.example.linkedinanalog.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.linkedinanalog.api.JobApiService
import com.example.linkedinanalog.data.models.job.JobModel
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(private val apiService: JobApiService) :
    Repository {
    var data = listOf<JobModel>()
        set(value) {
            field = value
            liveData.value = field
        }

    val liveData = MutableLiveData(data)

    override suspend fun getAll() {
        val response = apiService.getAllJobs()
        if (response.isSuccessful) {
            val body = response.body()
            data = body ?: listOf()
        } else data = emptyList()
    }
}