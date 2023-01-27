package com.example.linkedinanalog.data.db.dao.userDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.linkedinanalog.data.db.entity.userEntity.UserEntity


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users:List<UserEntity>)

    @Query("SELECT * FROM UserEntity")
    suspend fun getAllUsers():List<UserEntity>
}