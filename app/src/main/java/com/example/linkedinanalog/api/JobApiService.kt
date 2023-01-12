package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.job.JobModel
import retrofit2.Response
import retrofit2.http.GET

interface JobApiService {

    @GET("/api/my/jobs/")
    suspend fun getAllJobs(): Response<List<JobModel>>
}