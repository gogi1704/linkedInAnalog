package com.example.linkedinanalog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.linkedinanalog.data.db.dao.eventDao.EventDao
import com.example.linkedinanalog.data.db.dao.eventDao.EventRemoteKeyDao
import com.example.linkedinanalog.data.db.dao.userDao.UserDao
import com.example.linkedinanalog.data.db.dao.postDao.PostDao
import com.example.linkedinanalog.data.db.dao.postDao.PostRemoteKeyDao
import com.example.linkedinanalog.data.db.dao.wallDao.WallDao
import com.example.linkedinanalog.data.db.dao.wallDao.WallRemoteKeyDao
import com.example.linkedinanalog.data.db.entity.eventEntity.EventEntity
import com.example.linkedinanalog.data.db.entity.eventEntity.EventRemoteKeyEntity
import com.example.linkedinanalog.data.db.entity.postEntity.PostEntity
import com.example.linkedinanalog.data.db.entity.postEntity.PostRemoteKeyEntity
import com.example.linkedinanalog.data.db.entity.userEntity.UserEntity
import com.example.linkedinanalog.data.db.entity.wallEntity.WallEntity
import com.example.linkedinanalog.data.db.entity.wallEntity.WallRemoteKeyEntity

@Database(
    entities = [PostEntity::class, PostRemoteKeyEntity::class, WallEntity::class, WallRemoteKeyEntity::class,
        UserEntity::class, EventEntity::class, EventRemoteKeyEntity::class],
    version = 1
)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun wallDao(): WallDao
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
    abstract fun wallRemoteKeyDao(): WallRemoteKeyDao
    abstract fun eventRemoteKeyDao(): EventRemoteKeyDao


}