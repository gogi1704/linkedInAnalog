package com.example.linkedinanalog.viewModels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.auth.AppAuth
import com.example.linkedinanalog.auth.AuthState
import com.example.linkedinanalog.data.models.mediaModels.PhotoModel
import com.example.linkedinanalog.data.models.user.UserModel
import com.example.linkedinanalog.data.models.user.UserRequestModel
import com.example.linkedinanalog.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val appAuth: AppAuth,
    private val repository: AuthRepository
) : AndroidViewModel(application) {

    private var myUser = UserModel(-1, "", "", "")
        set(value) {
            field = value
            userLiveData.value = value
        }
    val userLiveData = MutableLiveData(myUser)

    private var showUser = UserModel(-1, "", "", "")
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



    fun getAllUsers() {
        viewModelScope.launch {
            usersData = repository.getAllUsers()
        }
    }


    fun registerUser(user: UserRequestModel) {
        viewModelScope.launch {
            val register = if (user.avatar == null) {
                repository.registerUser(user)
            } else
                repository.registerWithAvatar(user)

            appAuth.setAuth(register.id, register.token!!)
        }

    }


    fun authenticationUser(login: String, pass: String) {
        viewModelScope.launch {
            val register = repository.authenticationUser(login, pass)
            appAuth.setAuth(register.id, register.token!!)
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
            showUser = repository.getUserById(id)
        }
    }

    fun signOut() {
        repository.signOut()
    }

    companion object {
        const val AUTH_BUNDLE_KEY = "auth"
        const val AUTH_BUNDLE_VALUE_REG = "reg"
        const val AUTH_BUNDLE_VALUE_SIGN_IN = "signIn"

    }

}