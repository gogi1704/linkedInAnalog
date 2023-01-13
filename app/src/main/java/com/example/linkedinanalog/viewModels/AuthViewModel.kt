package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.auth.AppAuth
import com.example.linkedinanalog.auth.AuthState
import com.example.linkedinanalog.data.models.user.UserModel
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

    var myUser = UserModel(-1 , "" , "" ,"")

    val isAuth: Boolean
        get() = repository.isAuth

    val liveData: LiveData<AuthState>
        get() = repository.authData

    fun registerUser(login: String, pass: String, name: String, file: File?) {
        viewModelScope.launch {
            val register = repository.registerUser(login, pass, name, file)
            appAuth.setAuth(register.id, register.token!!)
        }

    }


    fun authenticationUser(login: String, pass: String){
        viewModelScope.launch {
            val register = repository.authenticationUser(login, pass)
            appAuth.setAuth(register.id , register.token!!)
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