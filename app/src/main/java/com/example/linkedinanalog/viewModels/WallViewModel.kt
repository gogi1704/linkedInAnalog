package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.data.repository.AuthRepository
import com.example.linkedinanalog.data.repository.PostRepositoryImpl
import com.example.linkedinanalog.data.repository.WallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallViewModel @Inject constructor(
    application: Application,
    private val wallRepository: WallRepository,
    private val authRepository: AuthRepository,
    private val postRepository: PostRepositoryImpl,
) :
    AndroidViewModel(application) {

    private val _pagingData = wallRepository.pagingData.cachedIn(viewModelScope).map {
        it.map { post ->
            post.toDto()
        }
    }

    val pagingData
        get() = _pagingData


    fun removeAll() {
        viewModelScope.launch {
            wallRepository.removeAll()
        }
    }

    fun like(id: Long, likeByMe: Boolean) {
        viewModelScope.launch {
            wallRepository.likeItem(id, likeByMe)
        }
    }


}