package com.example.storyapp.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.storyapp.data.repository.Repository

class RegisterViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository(application)
    fun signUp(name:String, email: String, password: String) = repository.register(name, email, password)
}