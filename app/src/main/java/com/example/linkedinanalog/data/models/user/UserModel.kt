package com.example.linkedinanalog.data.models.user

import com.example.linkedinanalog.data.db.entity.userEntity.UserEntity

data class UserModel(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String?
) {
    fun toEntity():UserEntity = UserEntity(id , login , name , avatar)



}

