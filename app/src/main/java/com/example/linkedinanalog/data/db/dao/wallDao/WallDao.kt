package com.example.linkedinanalog.data.db.dao.wallDao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.linkedinanalog.data.db.entity.postEntity.PostEntity
import com.example.linkedinanalog.data.db.entity.wallEntity.WallEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WallDao {

    @Query("SELECT * FROM WallEntity")
    fun getAll(): Flow<List<WallEntity>>

    @Query("SELECT * FROM WallEntity ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, WallEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWall(post: WallEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWall(list: List<WallEntity>)

    @Query("DELETE  FROM WallEntity")
    suspend fun removeAll()
}