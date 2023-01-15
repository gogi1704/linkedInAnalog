package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.data.repository.PostRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    application: Application,
    private val repository: PostRepositoryImpl
) : AndroidViewModel(application) {

    val liveData:MutableLiveData<List<PostModel>>
    get() = repository.liveData

    private val _pagingData = repository.pagingData.cachedIn(viewModelScope).map {
        it.map { post ->
            post.toDto()
        }
    }

    val pagingData
    get() = _pagingData

    fun getAllPosts() {
        viewModelScope.launch {
            repository.getAll()
        }
    }

}