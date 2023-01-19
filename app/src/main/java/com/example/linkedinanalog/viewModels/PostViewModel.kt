package com.example.linkedinanalog.viewModels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.linkedinanalog.data.models.MediaUpload
import com.example.linkedinanalog.data.models.mediaModels.PhotoModel
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.data.repository.PostRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    application: Application,
    private val repository: PostRepositoryImpl
) : AndroidViewModel(application) {

    private val _pagingData = repository.pagingData.cachedIn(viewModelScope).map {
        it.map { post ->
            post.toDto()
        }
    }

    val pagingData
        get() = _pagingData

    private val data:MutableLiveData<List<PostModel>>
    get() = repository.liveData


    private var photoModel = PhotoModel()
        set(value) {
            field = value
            _photoLiveData.value = value
        }
    private val _photoLiveData = MutableLiveData(photoModel)

    val photoLiveData
        get() = _photoLiveData

    val newerCount: LiveData<Int> = data.switchMap {
        val id = it.firstOrNull()?.id?.toLong() ?: 0L
        repository.getNewerItems((id))
            .catch { }
            .asLiveData(Dispatchers.Default)
    }




    fun getAllPosts() {
        viewModelScope.launch {
            repository.getAll()
        }
    }

    fun addPost(post: PostCreateRequest) {
        viewModelScope.launch {
            if (post.attachment == null) {
                repository.addItem(post)
            } else
                repository.addItemWithAttachments(post, MediaUpload(photoLiveData.value?.file!!))
        }

        photoLiveData.value = PhotoModel()
    }

    fun deletePost(id:Long){
        viewModelScope.launch {
            repository.deleteItem(id)
        }
    }


    fun changePhoto(uri: Uri?, file: File?) {
        photoModel = PhotoModel(uri, file)
    }


}