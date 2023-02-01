package com.example.linkedinanalog.data.repository

import com.example.linkedinanalog.data.models.job.JobModel

interface JobRepository {
    suspend fun getAll():List<JobModel>
    suspend fun addItem(item: JobModel)
    suspend fun getJobById(id: Long): List<JobModel>
    suspend fun deleteJob(id:Long)
}