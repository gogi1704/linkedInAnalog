package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.data.repository.AuthRepositoryImpl
import com.example.linkedinanalog.data.repository.JobRepositoryImpl
import com.example.linkedinanalog.exceptions.EventErrorState
import com.example.linkedinanalog.exceptions.EventErrorType
import com.example.linkedinanalog.exceptions.JobErrorState
import com.example.linkedinanalog.exceptions.JobErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
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

    private var jobErrorState = JobErrorState()
        set(value) {
            field = value
            _jobErrorStateLiveData.value = value
        }
    private val _jobErrorStateLiveData = MutableLiveData(jobErrorState)
    val jobErrorStateLiveData
        get() = _jobErrorStateLiveData

    fun clearMyUserJob() {
        myJobData = listOf()
    }


    fun getAllJobs() {
        viewModelScope.launch {
            try {
                myJobData = jobRepository.getAll()
            } catch (io: IOException) {
                jobErrorState = JobErrorState(errorType = JobErrorType.NetworkError)
            } catch (e: Exception) {

                jobErrorState = JobErrorState(errorType = JobErrorType.GetJobError)
            }
        }
        jobErrorState = JobErrorState()
    }

    fun getJobById(id: Long) {
        viewModelScope.launch() {
            try {
                userShowJobData = jobRepository.getJobById(id)
            } catch (io: IOException) {
                jobErrorState = JobErrorState(errorType = JobErrorType.NetworkError)
            } catch (e: Exception) {
                jobErrorState = JobErrorState(errorType = JobErrorType.GetJobError)
            }
        }
        jobErrorState = JobErrorState()
    }

    fun addJob(job: JobModel) {
        viewModelScope.launch {
            try {
                jobRepository.addItem(job)
                jobErrorState = JobErrorState(errorType = JobErrorType.AddJobComplete)
            } catch (io: IOException) {
                jobErrorState = JobErrorState(errorType = JobErrorType.NetworkError)
            } catch (e: Exception) {
                jobErrorState = JobErrorState(errorType = JobErrorType.AddJobError)
            }
        }
        jobErrorState = JobErrorState()
    }

    fun deleteJob(id: Long) {
        viewModelScope.launch {
            try {
                jobRepository.deleteJob(id)
                myJobData = myJobData.filter {
                    it.id != id.toInt()
                }
            } catch (io: IOException) {
                jobErrorState = JobErrorState(errorType = JobErrorType.NetworkError)
            } catch (e: Exception) {
                jobErrorState = JobErrorState(errorType = JobErrorType.AddJobError)
            }

        }
        jobErrorState = JobErrorState()
    }

}