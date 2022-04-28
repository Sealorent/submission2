package com.example.storyapp.data.response

import com.example.storyapp.data.model.User
import com.google.gson.annotations.SerializedName

data class  LoginResponse(

    @SerializedName("error")
    var error: Boolean,

    @SerializedName("message")
    var message: String,

    @SerializedName("loginResult")
    val loginResult: User
)

