package com.example.linkedinanalog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.linkedinanalog.data.db.dao.postDao.PostDao
import com.example.linkedinanalog.data.db.dao.postDao.PostRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.jobEntity.JobEntity
import com.example.linkedinanalog.data.db.entity.postEntity.PostEntity
import com.example.linkedinanalog.data.db.entity.postEntity.PostRemoteKeyEntity

@Database(entities = [PostEntity::class , PostRemoteKeyEntity::class , JobEntity::class] , version = 1)
abstract class AppDb():RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao

}