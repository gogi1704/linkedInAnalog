package com.example.linkedinanalog.data.db.entity.jobEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int ,
    val name:String,
    val position:String,
    val start:String,
    val finish:String?,
    val link:String?,

)
