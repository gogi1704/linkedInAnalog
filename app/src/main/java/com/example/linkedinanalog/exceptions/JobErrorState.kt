package com.example.linkedinanalog.exceptions

 data class JobErrorState (
     val loading: Boolean = false,
     val errorType: JobErrorType? = null,

     )

sealed class JobErrorType {
    object AddJobError : JobErrorType()
    object AddJobComplete : JobErrorType()
    object GetJobError : JobErrorType()
}