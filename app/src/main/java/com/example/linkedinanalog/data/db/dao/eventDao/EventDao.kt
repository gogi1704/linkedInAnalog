package com.example.linkedinanalog.data.db.dao.eventDao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.linkedinanalog.data.db.entity.eventEntity.EventEntity

@Dao
interface EventDao {


    @Query("SELECT * FROM EventEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: List<EventEntity>)

    @Query("DELETE FROM EventEntity WHERE id = :id")
    suspend fun deleteEvent(id:Int)

    @Query("DELETE FROM EventEntity")
    suspend fun removeAll()

}