package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.data.repository.AuthRepositoryImpl
import com.example.linkedinanalog.data.repository.JobRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    private val jobRepository: JobRepositoryImpl,
    application: Application
) : AndroidViewModel(application) {

    private var myJobData = listOf<JobModel>()
        set(value) {
            field = value
            myJobLiveData.value = field
        }
    val myJobLiveData = MutableLiveData(myJobData)

    private var userShowJobData = listOf<JobModel>()
        set(value) {
            field = value
            userShowJobLiveData.value = field
        }
    val userShowJobLiveData = MutableLiveData(userShowJobData)

    fun clearMyUserJob(){
        myJobData = listOf()
    }


    fun getAllJobs() {
        viewModelScope.launch {
            myJobData = jobRepository.getAll()
        }
    }

    fun getJobById(id: Long) {
        viewModelScope.launch() {
            userShowJobData = jobRepository.getJobById(id)
        }
    }

    fun addJob(job: JobModel) {
        viewModelScope.launch {
            jobRepository.addItem(job)
        }
    }

}