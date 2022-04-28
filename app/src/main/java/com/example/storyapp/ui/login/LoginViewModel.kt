package com.example.storyapp.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.storyapp.data.repository.Repository

class LoginViewModel(application: Application) : AndroidViewModel(application){
    private val repository = Repository(application)
    fun signIn(email: String , password : String) = repository.login(email, password)
}