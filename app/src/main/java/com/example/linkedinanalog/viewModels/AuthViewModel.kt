package com.example.linkedinanalog.viewModels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.auth.AppAuth
import com.example.linkedinanalog.auth.AuthState
import com.example.linkedinanalog.data.media.mediaModels.PhotoModel
import com.example.linkedinanalog.data.models.user.UserModel
import com.example.linkedinanalog.data.models.user.UserRequestModel
import com.example.linkedinanalog.data.repository.AuthRepositoryImpl
import com.example.linkedinanalog.exceptions.AuthErrorState
import com.example.linkedinanalog.exceptions.AuthErrorType
import com.example.linkedinanalog.exceptions.NetworkError
import com.example.linkedinanalog.exceptions.UnknownError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

private val emptyUser = UserModel(-1, "", "", "")

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val appAuth: AppAuth,
    private val repository: AuthRepositoryImpl
) : AndroidViewModel(application) {

    private var myUser = emptyUser
        set(value) {
            field = value
            userLiveData.value = value
        }
    val userLiveData = MutableLiveData(myUser)

    private var showUser = emptyUser
        set(value) {
            field = value
            showUserLiveData.value = value
        }
    val showUserLiveData = MutableLiveData(showUser)

    private var usersData: List<UserModel> = listOf()
        set(value) {
            field = value
            _usersLiveData.value = value
        }

    private val _usersLiveData = MutableLiveData(usersData)

    val usersLiveData
        get() = _usersLiveData

    val isAuth: Boolean
        get() = repository.isAuth

    val authLiveData: LiveData<AuthState>
        get() = repository.authData

    private var photoModel = PhotoModel()
        set(value) {
            field = value
            _photoLiveData.value = value
        }

    private val _photoLiveData = MutableLiveData(photoModel)
    val photoLiveData
        get() = _photoLiveData

    private var participantsOrSpeakersData = mutableListOf<UserModel>()
        set(value) {
            field = value
            _participantsOrSpeakerLiveData.value = value
        }

    private val _participantsOrSpeakerLiveData = MutableLiveData(participantsOrSpeakersData)
    val participantsOrSpeakerLiveData
        get() = _participantsOrSpeakerLiveData

    private var errorState = AuthErrorState()
        set(value) {
            field = value
            _errorStateLiveData.value = value
        }
    private val _errorStateLiveData = MutableLiveData(errorState)
    val errorStateLiveData
        get() = _errorStateLiveData


    fun getAllUsers() {
        viewModelScope.launch {
            try {
                usersData = repository.getAllUsers()
            } catch (net: NetworkError) {
                errorState = AuthErrorState(errorType = AuthErrorType.NetworkError)
            }
            errorState = AuthErrorState()
        }

    }


    fun registerUser(user: UserRequestModel) {
        viewModelScope.launch {
            try {
                val register = if (user.avatar == null) {
                    repository.registerUser(user)
                } else
                    repository.registerWithAvatar(user)
                errorState = errorState.copy(errorType = AuthErrorType.AuthOk)
                appAuth.setAuth(register.id, register.token!!)
            } catch (net: NetworkError) {
                errorState = AuthErrorState(errorType = AuthErrorType.NetworkError)

            } catch (e: Exception) {
                errorState = errorState.copy(errorType = AuthErrorType.RegisterError)
            }
            errorState = AuthErrorState()
        }
    }


    fun authenticationUser(login: String, pass: String) {
        viewModelScope.launch {
            try {
                val register = repository.authenticationUser(login, pass)
                appAuth.setAuth(register.id, register.token!!)
                errorState = AuthErrorState(errorType = AuthErrorType.AuthOk)

            } catch (net: NetworkError) {
                errorState = AuthErrorState(errorType = AuthErrorType.NetworkError)
            } catch (e: UnknownError) {
                errorState = AuthErrorState(errorType = AuthErrorType.AuthError)
            }
            errorState = AuthErrorState()
        }

    }

    fun changePhoto(uri: Uri?, file: File?) {
        photoModel = PhotoModel(uri, file)
    }


    fun updateMyUser() {
        viewModelScope.launch {
            myUser = repository.updateMyUser()
        }
    }

    fun getUserById(id: Long) {
        viewModelScope.launch {
            try {
                showUser = repository.getUserById(id)
            } catch (net: NetworkError) {
                errorState = AuthErrorState(errorType = AuthErrorType.NetworkError)
            } catch (e: Exception) {
                errorState = AuthErrorState(errorType = AuthErrorType.GetUserError)
            }
            errorState = AuthErrorState()
        }

    }

    fun getUsersList(listId: List<Int>) {
        val list = mutableListOf<UserModel>()
        for (user in usersData) {
            for (id in listId) {
                if (user.id == id) {
                    list.add(user)
                }
            }
        }
        participantsOrSpeakersData = list
    }

    fun signOut() {
        repository.signOut()
        errorState = AuthErrorState()
        myUser = emptyUser
    }

    companion object {
        const val AUTH_BUNDLE_KEY = "auth"
        const val AUTH_BUNDLE_VALUE_REG = "reg"
        const val AUTH_BUNDLE_VALUE_SIGN_IN = "signIn"

    }

}