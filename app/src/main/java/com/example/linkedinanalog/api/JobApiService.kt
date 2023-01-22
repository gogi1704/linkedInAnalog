package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.job.JobModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JobApiService {

    @GET("/api/my/jobs/")
    suspend fun getAllJobs(): Response<List<JobModel>>

    @POST("/api/my/jobs/")
    suspend fun addJob(@Body job:JobModel):Response<JobModel>
}