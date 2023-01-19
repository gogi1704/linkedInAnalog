package com.example.linkedinanalog.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.linkedinanalog.data.db.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, PostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(posts: List<PostEntity>)

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getPagingData(): PagingSource<Int, PostEntity>

    @Query(
        """
        DELETE FROM PostEntity WHERE id = :id
    """
    )
    suspend fun deletePost(id: Long)

    @Query("DELETE FROM PostEntity")
    suspend fun removeAll()


}