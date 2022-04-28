package com.example.storyapp.ui.story

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.model.Story
import com.example.storyapp.data.repository.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel (application: Application): AndroidViewModel(application){
    private val repository = Repository(application)

    fun getStories(token: String):LiveData<PagingData<Story>> = repository.getAllStoriesWithLocation(token).cachedIn(viewModelScope).asLiveData()
    fun addStories(token: String, file: MultipartBody.Part, description: RequestBody) = repository.addNewStory(token,file,description)

}