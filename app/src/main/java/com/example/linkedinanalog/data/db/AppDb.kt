package com.example.linkedinanalog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.linkedinanalog.data.db.dao.PostDao
import com.example.linkedinanalog.data.db.entity.PostEntity

@Database(entities = [PostEntity::class] , version = 1)
abstract class AppDb():RoomDatabase() {
    abstract fun postDao(): PostDao

}