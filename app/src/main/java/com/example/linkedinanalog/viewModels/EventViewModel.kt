package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.linkedinanalog.data.models.MediaUpload
import com.example.linkedinanalog.data.models.event.EventCreateRequest
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.repository.EventRepositoryImpl
import com.example.linkedinanalog.exceptions.EventErrorState
import com.example.linkedinanalog.exceptions.EventErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    application: Application,
    private val eventRepository: EventRepositoryImpl
) : AndroidViewModel(application) {

    private val _pagingData = eventRepository.pagingData.cachedIn(viewModelScope).map {
        it.map { event ->
            event.toDto()
        }
    }
    val pagingData
        get() = _pagingData

    private var eventErrorState = EventErrorState()
        set(value) {
            field = value
            _eventErrorStateLiveData.value = value
        }
    private val _eventErrorStateLiveData = MutableLiveData(eventErrorState)
    val eventErrorStateLiveData
        get() = _eventErrorStateLiveData


    fun addToChooseList(id: Int) {
        eventRepository.addChooseUser(id)
    }


    fun createEvent(event: EventCreateRequest, mediaUpload: MediaUpload?) {
        viewModelScope.launch {
            try {
                eventErrorState = EventErrorState(loading = true)
                if (event.attachment == null) {
                    eventRepository.createEvent(event)
                } else
                    eventRepository.createWithAttachments(event, mediaUpload)
                eventErrorState = EventErrorState(errorType = EventErrorType.CreateOk)
            } catch (e: Exception) {
                eventErrorState = EventErrorState(errorType = EventErrorType.CreateError)
            }

        }
        eventErrorState = EventErrorState()
    }

    fun participantByMe(id: Long, isParticipatedByMe: Boolean) {
        viewModelScope.launch {
            try {
                eventRepository.participantByMe(id, isParticipatedByMe)
            } catch (e: Exception) {
                eventErrorState = EventErrorState(errorType = EventErrorType.ParticipantError)
            }
        }
        eventErrorState = EventErrorState()
    }

}