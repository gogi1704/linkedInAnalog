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

}