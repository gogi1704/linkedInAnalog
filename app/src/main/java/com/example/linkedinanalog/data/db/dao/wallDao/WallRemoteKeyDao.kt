package com.example.linkedinanalog.data.db.dao.wallDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.linkedinanalog.data.db.entity.wallEntity.WallRemoteKeyEntity

@Dao
interface WallRemoteKeyDao{

    @Query("SELECT COUNT(*) == 0 FROM WallRemoteKeyEntity")
    suspend fun isEmpty(): Boolean

    @Query("SELECT MAX(id) FROM WallRemoteKeyEntity")
    suspend fun max(): Long?

    @Query("SELECT MIN(id) FROM WallRemoteKeyEntity")
    suspend fun min(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: WallRemoteKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: List<WallRemoteKeyEntity>)

    @Query("DELETE FROM WallRemoteKeyEntity")
    suspend fun removeAll()
}