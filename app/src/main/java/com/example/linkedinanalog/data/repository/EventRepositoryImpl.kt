package com.example.linkedinanalog.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.linkedinanalog.api.EventApiService
import com.example.linkedinanalog.data.models.event.EventModel
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val apiService: EventApiService) :
    Repository<EventModel> {

    var data = listOf<EventModel>()
        set(value) {
            field = value
            liveData.value = value
        }
    val liveData = MutableLiveData(data)

    override suspend fun getAll() {
        val response = apiService.getAllEvents()
        if (response.isSuccessful) {
            val body = response.body()
            data = body ?: listOf()
        } else throw Exception()

    }

    override suspend fun addItem(item: EventModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem(id: Long) {
        TODO("Not yet implemented")
    }
}
