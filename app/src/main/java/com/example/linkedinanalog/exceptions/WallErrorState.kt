package com.example.linkedinanalog.exceptions

data class WallErrorState(
    val loading: Boolean = false,
    val errorType: WallErrorType? = null,

    )

sealed class WallErrorType {
    object WallRemoveError : WallErrorType()
    object WallLikeError : WallErrorType()
}
