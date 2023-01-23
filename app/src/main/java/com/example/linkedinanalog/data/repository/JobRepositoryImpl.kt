package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.api.JobApiService
import com.example.linkedinanalog.data.models.job.JobModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(private val apiService: JobApiService) :
    Repository<JobModel> {


    override suspend fun getAll():List<JobModel> {
        //todo
        val response = apiService.getAllMyJobs()
        if (response.isSuccessful) {
            val body = response.body()
           return body ?: listOf()
        } else return  emptyList()
    }

    override suspend fun addItem(item: JobModel) {
        val response = apiService.addJob(item)
        if (response.isSuccessful){

        } else throw Exception()

    }

    suspend fun getJobById(id:Long):List<JobModel>{
        //todo
        val response = apiService.getJobById(id)
        if (response.isSuccessful){
            return response.body()!!
        }else throw Exception()
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