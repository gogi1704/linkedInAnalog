package com.example.linkedinanalog.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.linkedinanalog.api.JobApiService
import com.example.linkedinanalog.data.models.job.JobModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(private val apiService: JobApiService) :
    Repository<JobModel> {
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

    override suspend fun addItem(item: JobModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getNewerItems(id: Long): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun likeItem(id: Long, likeByMe: Boolean) {
        TODO("Not yet implemented")
    }
}