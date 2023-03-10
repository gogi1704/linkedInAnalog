package com.example.linkedinanalog.exceptions

data class PostErrorState(
    val loading: Boolean = false,
    val errorType: PostErrorType? = null,

    )

sealed class PostErrorType {
    object AddPostError : PostErrorType()
    object DeletePostError : PostErrorType()
    object AddPostComplete : PostErrorType()
    object LikePostError:PostErrorType()
    object NetworkError : PostErrorType()

}