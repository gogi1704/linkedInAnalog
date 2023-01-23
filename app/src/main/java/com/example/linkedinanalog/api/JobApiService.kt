package com.example.linkedinanalog.api

import com.example.linkedinanalog.data.models.job.JobModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JobApiService {

    @GET("/api/my/jobs/")
    suspend fun getAllMyJobs(): Response<List<JobModel>>

    @POST("/api/my/jobs/")
    suspend fun addJob(@Body job:JobModel):Response<JobModel>

    @GET("/api/{id}/jobs/")
    suspend fun getJobById(@Path("id") id:Long):Response<List<JobModel>>
}