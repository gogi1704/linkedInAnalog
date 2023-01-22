package com.example.linkedinanalog.data.models.user

import java.io.File

data class UserRequestModel (
    val login:String,
    val pass:String,
    val name:String,
    val avatar:File?
        )