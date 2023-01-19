package com.example.linkedinanalog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.linkedinanalog.data.db.dao.PostDao
import com.example.linkedinanalog.data.db.dao.PostRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.PostEntity
import com.example.linkedinanalog.data.db.entity.PostRemoteKeyEntity

@Database(entities = [PostEntity::class , PostRemoteKeyEntity::class] , version = 1)
abstract class AppDb():RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao

}