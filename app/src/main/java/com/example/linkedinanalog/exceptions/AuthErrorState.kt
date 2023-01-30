package com.example.linkedinanalog.exceptions

data class AuthErrorState(
    val loading:Boolean= false,
    val errorType:AuthErrorType ? = null,

    )

sealed class AuthErrorType {
    object RegisterError : AuthErrorType()
    object AuthError : AuthErrorType()
    object UserNotFoundError : AuthErrorType()
    object AuthOk : AuthErrorType()

}




