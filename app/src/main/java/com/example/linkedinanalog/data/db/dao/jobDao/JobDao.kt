package com.example.linkedinanalog.data.db.dao.jobDao

import androidx.room.Dao
import androidx.room.Query
import com.example.linkedinanalog.data.db.entity.jobEntity.JobEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {

    @Query("SELECT * FROM JobEntity")
    fun getAll(): Flow<List<JobEntity>>

}