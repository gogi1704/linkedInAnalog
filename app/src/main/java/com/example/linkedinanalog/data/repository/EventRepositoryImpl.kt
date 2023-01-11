package com.example.linkedinanalog.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.linkedinanalog.api.ApiService
import com.example.linkedinanalog.data.models.event.EventModel
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    EventRepository {

    var data = listOf<EventModel>()
        set(value) {
            field = value
            liveData.value = value
        }
    val liveData = MutableLiveData(data)

    override suspend fun getEvents() {
        val response = apiService.getEvents()
        if (response.isSuccessful) {
            val body = response.body()
            data = body ?: listOf()
        } else throw Exception()

    }
}
