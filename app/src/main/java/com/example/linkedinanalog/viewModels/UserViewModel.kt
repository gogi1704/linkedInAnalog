package com.example.linkedinanalog.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

}