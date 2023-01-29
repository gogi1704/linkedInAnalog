package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.api.JobApiService
import com.example.linkedinanalog.data.models.job.JobModel
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(private val jobApiService: JobApiService) {



    suspend fun getAll():List<JobModel> {
        //todo
        val response = jobApiService.getAllMyJobs()
        if (response.isSuccessful) {
            return response.body()!!
        } else return listOf()
    }

    suspend fun addItem(item: JobModel) {
        val response = jobApiService.addJob(item)
        if (response.isSuccessful) {

        } else throw Exception()

    }

    suspend fun getJobById(id: Long): List<JobModel> {
        //todo
        val response = jobApiService.getJobById(id)
        if (response.isSuccessful) {
            return response.body()!!
        } else throw Exception()
    }


}