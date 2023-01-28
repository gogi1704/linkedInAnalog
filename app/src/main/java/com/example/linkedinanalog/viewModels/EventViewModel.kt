package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.data.models.MediaUpload
import com.example.linkedinanalog.data.models.event.EventCreateRequest
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.repository.EventRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    application: Application,
    private val eventRepository: EventRepositoryImpl
) : AndroidViewModel(application) {

    var eventData = listOf<EventModel>()
        set(value) {
            field = value
            _liveData.value = value
        }
    private val _liveData = MutableLiveData(eventData)

    val liveData
        get() = _liveData


    fun addToChooseList(id: Int) {
        eventRepository.addChooseUser(id)
    }


    fun createEvent(event: EventCreateRequest, mediaUpload: MediaUpload?) {
        viewModelScope.launch {
            if (event.attachment == null) {
                eventRepository.createEvent(event)
            } else
                eventRepository.createWithAttachments(event, mediaUpload)
        }

    }

    fun participantByMe(id: Long, isParticipatedByMe: Boolean) {
        viewModelScope.launch {
            eventRepository.participantByMe(id, isParticipatedByMe)
        }
    }


    fun getEvents() {
        viewModelScope.launch {
            eventData = eventRepository.getAll()
        }
    }
}