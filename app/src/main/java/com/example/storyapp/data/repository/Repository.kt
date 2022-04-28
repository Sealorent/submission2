package com.example.storyapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.storyapp.data.Resource
import com.example.storyapp.data.model.Story
import com.example.storyapp.data.remote.ApiService
import com.example.storyapp.data.remote.RetrofitInstance
import com.example.storyapp.data.remote.PagingStorySource
import com.example.storyapp.data.response.AddStoriesResponse
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.util.Const.KEY_EMAIL
import com.example.storyapp.util.Const.KEY_IS_LOGIN
import com.example.storyapp.util.Const.KEY_TOKEN
import com.example.storyapp.util.Const.KEY_USER_ID
import com.example.storyapp.util.Const.KEY_USER_NAME
import com.example.storyapp.util.SessionManager
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class Repository(private val application: Application) {

    private val retrofit: ApiService
    private lateinit var pref: SessionManager


    init {
        retrofit = RetrofitInstance.getRetrofitInstance()
        pref = SessionManager(application)

    }


    fun login(email : String, password : String): LiveData<Resource<LoginResponse>>{
        val Log = MutableLiveData<Resource<LoginResponse>>()

        Log.postValue(Resource.Loading())
        retrofit.login(email,password).enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.postValue(Resource.Error(t.message))

            }
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.code() == 200) {
                    val res = response.body()
                    val data = response.body()?.loginResult
                    if (res == null)
                        Log.postValue(Resource.Error(null))
                    else
                        Log.postValue(Resource.Success(res))
                        pref.apply {
                            pref.setStringPreference(KEY_USER_ID,data?.id.toString())
                            pref.setStringPreference(KEY_TOKEN,data?.token.toString())
                            pref.setStringPreference(KEY_USER_NAME,data?.name.toString())
                            pref.setStringPreference(KEY_EMAIL,email)
                            pref.setBooleanPreference(KEY_IS_LOGIN,true)
                        }
                }else {
                    val res = response.body()
                    if (res == null)
                        Log.postValue(Resource.Error(null))
                    else
                        Log.postValue(Resource.Success(res))
                }
            }
        })
        return  Log
    }


    fun register(name: String,email : String, password : String): LiveData<Resource<LoginResponse>>{
        val Log= MutableLiveData<Resource<LoginResponse>>()

        Log.postValue(Resource.Loading())
        retrofit.register(name,email,password).enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.postValue(Resource.Error(t.message))
            }
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.code() == 200) {
                    val data = response.body()
                    if (data == null)
                        Log.postValue(Resource.Error(null))
                    else
                        Log.postValue(Resource.Success(data))
                }else if (response.code() == 201) {
                    val data = response.body()
                    if (data == null)
                        Log.postValue(Resource.Error(null))
                    else
                        Log.postValue(Resource.Success(data))
                }
            }
        })
        return Log
    }



    fun getAllStoriesWithLocation(token: String): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
           pagingSourceFactory = {
            PagingStorySource(
                retrofit,
                "Bearer"+" "+ token
            )
           }
        ).flow
    }
//    fun getAllStories(token : String): LiveData<Resource<GetStoriesResponse>>{
//        val data = MutableLiveData<Resource<GetStoriesResponse>>()
//
//        data.postValue(Resource.Loading())
//        retrofit.getAllStories("Bearer" +" "+ token).enqueue(object : retrofit2.Callback<GetStoriesResponse>{
//            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
//                data.postValue(Resource.Error(t.message))
//            }
//
//            override fun onResponse(
//                call: Call<GetStoriesResponse>,
//                response: Response<GetStoriesResponse>
//            ) {
//                if (response.code() == 200) {
//                    val res = response.body()
//                    if (res == null)
//                        data.postValue(Resource.Error(null))
//                    else
//                        data.postValue(Resource.Success(res))
//                }else if (response.code() == 201) {
//                    val res = response.body()
//                    if (res == null)
//                        data.postValue(Resource.Error(null))
//                    else
//                        data.postValue(Resource.Success(res))
//                }
//            }
//        })
//        return data
//    }


    fun addNewStory(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<Resource<AddStoriesResponse>>{
        val data = MutableLiveData<Resource<AddStoriesResponse>>()

        data.postValue(Resource.Loading())
        retrofit.addNewStories("Bearer" +" "+ token,file,description).enqueue(object : retrofit2.Callback<AddStoriesResponse>{
            override fun onFailure(call: Call<AddStoriesResponse>, t: Throwable) {
                data.postValue(Resource.Error(t.message))
            }

            override fun onResponse(
                call: Call<AddStoriesResponse>,
                response: Response<AddStoriesResponse>
            ) {
                if (response.code() == 200) {
                    val res = response.body()
                    if (res == null)
                        data.postValue(Resource.Error(null))
                    else
                        data.postValue(Resource.Success(res))
                }else if (response.code() == 201) {
                    val res = response.body()
                    if (res == null)
                        data.postValue(Resource.Error(null))
                    else
                        data.postValue(Resource.Success(res))
                }
            }
        })
        return data
    }

    private fun generateBearerToken(token: String): String {
        return "Bearer $token"
    }

}