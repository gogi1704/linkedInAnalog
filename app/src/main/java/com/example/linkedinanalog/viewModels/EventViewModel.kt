package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.repository.EventRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    application: Application,
    private val repository: EventRepositoryImpl
) : AndroidViewModel(application) {

    val liveData:MutableLiveData<List<EventModel>>
    get() {
        return repository.liveData
    }

    fun getEvents() {
        viewModelScope.launch {
            repository.getEvents()
        }
    }
}