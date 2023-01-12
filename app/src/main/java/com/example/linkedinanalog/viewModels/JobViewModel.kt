package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.data.repository.JobRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val repository: JobRepositoryImpl,
    application: Application
) : AndroidViewModel(application) {

    val liveData: MutableLiveData<List<JobModel>>
        get() = repository.liveData

    fun getAllJobs() {
        viewModelScope.launch {
            repository.getAll()
        }
    }


}