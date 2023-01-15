package com.example.linkedinanalog.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.linkedinanalog.data.db.entity.PostEntity

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post:PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(posts:List<PostEntity>)

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getPagingData(): PagingSource<Int, PostEntity>




}