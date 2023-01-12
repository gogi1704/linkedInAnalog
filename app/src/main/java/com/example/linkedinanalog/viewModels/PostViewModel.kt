package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.data.repository.PostRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    application: Application,
    private val repository: PostRepositoryImpl
) : AndroidViewModel(application) {

    val liveData:MutableLiveData<List<PostModel>>
    get() = repository.liveData

    fun getAllPosts() {
        viewModelScope.launch {
            repository.getAll()
        }
    }

}