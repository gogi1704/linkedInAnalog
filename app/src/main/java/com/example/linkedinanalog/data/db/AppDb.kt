package com.example.linkedinanalog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.linkedinanalog.data.db.dao.jobDao.JobDao
import com.example.linkedinanalog.data.db.dao.postDao.PostDao
import com.example.linkedinanalog.data.db.dao.postDao.PostRemoteKeyDao
import com.example.linkedinanalog.data.db.dao.wallDao.WallDao
import com.example.linkedinanalog.data.db.dao.wallDao.WallRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.jobEntity.JobEntity
import com.example.linkedinanalog.data.db.entity.postEntity.PostEntity
import com.example.linkedinanalog.data.db.entity.postEntity.PostRemoteKeyEntity
import com.example.linkedinanalog.data.db.entity.wallEntity.WallEntity
import com.example.linkedinanalog.data.db.entity.wallEntity.WallRemoteKeyEntity

@Database(
    entities = [PostEntity::class, PostRemoteKeyEntity::class, JobEntity::class, WallEntity::class, WallRemoteKeyEntity::class],
    version = 1
)
abstract class AppDb() : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun wallDao(): WallDao
    abstract fun jobDao(): JobDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
    abstract fun wallRemoteKeyDao(): WallRemoteKeyDao


}