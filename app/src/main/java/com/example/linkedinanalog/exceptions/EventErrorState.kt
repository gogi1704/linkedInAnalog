package com.example.linkedinanalog.exceptions

data class EventErrorState(
    val loading:Boolean= false,
    val errorType:EventErrorType ? = null,

    )

sealed class EventErrorType {
    object CreateError : EventErrorType()
    object ParticipantError : EventErrorType()
    object CreateComplete : EventErrorType()
    object NetworkError : EventErrorType()

}
