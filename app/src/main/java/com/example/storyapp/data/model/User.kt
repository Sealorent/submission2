package com.example.storyapp.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userId")
    var id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("token")
    var token: String,
)
