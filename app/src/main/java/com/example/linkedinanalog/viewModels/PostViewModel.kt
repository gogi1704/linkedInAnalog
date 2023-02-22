package com.example.linkedinanalog.viewModels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.linkedinanalog.data.models.MediaUpload
import com.example.linkedinanalog.data.media.mediaModels.AudioModel
import com.example.linkedinanalog.data.media.mediaModels.PhotoModel
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.repository.PostRepositoryImpl
import com.example.linkedinanalog.exceptions.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
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




    private var photoModel = PhotoModel()
        set(value) {
            field = value
            _photoLiveData.value = value
        }
    private val _photoLiveData = MutableLiveData(photoModel)

    val photoLiveData
        get() = _photoLiveData


    private var audioModel = AudioModel()
        set(value) {
            field = value
            _audioLiveData.value = value
        }
    private val _audioLiveData = MutableLiveData(audioModel)


//    val newerCount: LiveData<Int> = _dataFlow.switchMap { it ->
//        val id = it.lastOrNull()?.id?.toLong() ?: 0L
//        repository.getNewerItems((id))
//            .catch {
//                it.printStackTrace()
//            }
//            .asLiveData(Dispatchers.Default)
//    }

    private var postErrorState = PostErrorState()
        set(value) {
            field = value
            _postErrorStateLiveData.value = value
        }

    private val _postErrorStateLiveData = MutableLiveData(postErrorState)
    val postErrorStateLiveData
        get() = _postErrorStateLiveData


    fun addPost(post: PostCreateRequest) {
        viewModelScope.launch {
            try {
                if (post.attachment == null || post.attachment.url == photoModel.uri.toString()) {
                    repository.addItem(post)
                } else {
                    repository.addItemWithAttachments(
                        post,
                        MediaUpload(photoLiveData.value?.file!!)
                    )
                }
                postErrorState = PostErrorState(errorType = PostErrorType.AddPostComplete)
            } catch (io: IOException) {
                postErrorState = PostErrorState(errorType = PostErrorType.NetworkError)
            } catch (e: Exception) {
                postErrorState = PostErrorState(errorType = PostErrorType.LikePostError)
            }
            postErrorState = PostErrorState()
            photoLiveData.value = PhotoModel()
        }

    }

    fun deletePost(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteItem(id)
            } catch (io: IOException) {
                postErrorState = PostErrorState(errorType = PostErrorType.NetworkError)
            } catch (e: Exception) {
                postErrorState = PostErrorState(errorType = PostErrorType.DeletePostError)
            }
            postErrorState = PostErrorState()
        }

    }


    fun changePhoto(uri: Uri?, file: File?) {
        photoModel = PhotoModel(uri, file)
    }



    fun like(id: Long, likeByMe: Boolean) {
        viewModelScope.launch {
            try {
                repository.likeItem(id, likeByMe)
            } catch (io: IOException) {
                postErrorState = PostErrorState(errorType = PostErrorType.NetworkError)
            } catch (e: Exception) {
                postErrorState = PostErrorState(errorType = PostErrorType.LikePostError)
            }
            postErrorState = PostErrorState()
        }


    }


}