package com.example.storyapp.data.remote

import com.example.storyapp.data.response.AddStoriesResponse
import com.example.storyapp.data.response.GetStoriesResponse
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.util.Const
import com.example.storyapp.util.Const.REGISTER_URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST(Const.LOGIN_URL)
    fun login( @Field("email") email: String,@Field("password") password: String): Call<LoginResponse>

    @FormUrlEncoded
    @POST(REGISTER_URL)
    fun register(@Field("name") name: String,@Field("email") email: String,@Field("password") password: String):Call<LoginResponse>

    @GET("stories")
     fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): GetStoriesResponse

    @Multipart
    @POST("stories")
     fun addNewStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddStoriesResponse>
}