package com.example.linkedinanalog.data.db.dao.jobDao

import androidx.room.Dao
import com.example.linkedinanalog.data.db.entity.jobEntity.JobEntity
import retrofit2.Response

@Dao
interface JobDao {


    suspend fun getMyJobs():Response<List<JobEntity>>

}