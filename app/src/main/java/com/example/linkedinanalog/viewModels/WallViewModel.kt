package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.data.repository.WallRepositoryImpl
import com.example.linkedinanalog.exceptions.WallErrorState
import com.example.linkedinanalog.exceptions.WallErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WallViewModel @Inject constructor(
    application: Application,
    private val wallRepository: WallRepositoryImpl,
) :
    AndroidViewModel(application) {

    private val _pagingData = wallRepository.pagingData.cachedIn(viewModelScope).map {
        it.map { post ->
            post.toDto()
        }
    }
    val pagingData
        get() = _pagingData

    private var wallErrorState = WallErrorState()
        set(value) {
            field = value
            _wallErrorStateLiveData.value = value
        }
    private val _wallErrorStateLiveData = MutableLiveData(wallErrorState)
    val wallErrorStateLiveData
        get() = _wallErrorStateLiveData

    var data = listOf<PostModel>()
    set(value) {
        field = value
        liveData.value = value
    }
    val liveData = MutableLiveData(data)


    fun removeAll() {
        viewModelScope.launch {
            try {
                wallRepository.removeAll()
            } catch (e: Exception) {
                wallErrorState = WallErrorState(errorType = WallErrorType.WallRemoveError)
            }
            wallErrorState = WallErrorState()

        }
    }

    fun getAll(id: Long) {


        viewModelScope.launch {
            data = wallRepository.getAll(id)
        }

    }

    fun like(id: Long, likeByMe: Boolean) {
        viewModelScope.launch {
            try {
                wallRepository.likeItem(id, likeByMe)
            } catch (io: IOException) {
                wallErrorState = WallErrorState(errorType = WallErrorType.NetworkError)
            } catch (e: Exception) {
                wallErrorState = WallErrorState(errorType = WallErrorType.WallLikeError)
            }
            wallErrorState = WallErrorState()

        }
    }


}