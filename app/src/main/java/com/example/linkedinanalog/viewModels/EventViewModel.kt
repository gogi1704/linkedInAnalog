package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.data.models.MediaUpload
import com.example.linkedinanalog.data.models.event.EventCreateRequest
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.models.user.UserModel
import com.example.linkedinanalog.data.repository.EventRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    application: Application,
    private val eventRepository: EventRepositoryImpl
) : AndroidViewModel(application) {



    val liveData: MutableLiveData<List<EventModel>>
        get() {
            return eventRepository.liveData
        }

    fun addToChooseList(id: Int) {
        eventRepository.addChooseUser(id)
    }


    fun createEvent(event: EventCreateRequest , mediaUpload: MediaUpload?) {
        viewModelScope.launch {
            if (event.attachment == null) {
                eventRepository.createEvent(event)
            } else
                eventRepository.createWithAttachments(event, mediaUpload)
        }

    }






    fun getEvents() {
        viewModelScope.launch {
            eventRepository.getAll()
        }
    }
}