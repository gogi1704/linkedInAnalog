package com.example.linkedinanalog.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkedinanalog.auth.AppAuth
import com.example.linkedinanalog.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val appAuth: AppAuth,
    private val repository: AuthRepository
) : AndroidViewModel(application) {



    fun registerUser(login: String, pass: String, name: String, file: String?) {
        viewModelScope.launch {
            val register = repository.registerUser(login, pass, name, file)
            appAuth.setAuth(register.id , register.token!!)
        }

    }

}