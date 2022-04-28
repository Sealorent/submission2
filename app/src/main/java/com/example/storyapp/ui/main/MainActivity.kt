package com.example.storyapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.data.Resource
import com.example.storyapp.data.model.Story
import com.example.storyapp.data.response.GetStoriesResponse
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.ProfileActivity
import com.example.storyapp.ui.location.MapsActivity
import com.example.storyapp.ui.story.LoadingStateAdapter
import com.example.storyapp.ui.story.StoryAdapter
import com.example.storyapp.ui.story.StoryViewModel
import com.example.storyapp.ui.story.add.AddStoryActivity
import com.example.storyapp.util.SessionManager
import com.example.storyapp.util.ViewStateCallback
import com.example.storyapp.util.ext.animateVisibility

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val viewModel by viewModels<StoryViewModel>()
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var pref: SessionManager
    private lateinit var recyclerView: RecyclerView


    private var token: String? = null
    private var nameUser: String? = null


    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)



        pref = SessionManager(this)
        token = pref.getToken
        nameUser = pref.getUserName
        print(token.toString())
        setRecyclerView()
        getAllStories(token.toString())
        setSwipeRefreshLayout(token.toString())
        initAction()
    }


    private fun initAction() {
        mainBinding.fabNewStory.setOnClickListener {
            AddStoryActivity.start(this)
        }
        mainBinding.btnAccount.setOnClickListener {
            ProfileActivity.start(this)
        }
        mainBinding.btnGoogleMap.setOnClickListener{
            MapsActivity.start(this)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }
            else -> false
        }
    }
    private fun getAllStories(token:String){
        viewModel.getStories(token).observe(this){
           result -> updateRecyclerViewData(result)
        }
    }

    private fun setSwipeRefreshLayout(token: String) {
        mainBinding.swipeRefresh.setOnRefreshListener {
            getAllStories(token)
        }
    }

    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        storyAdapter = StoryAdapter()

        // Pager LoadState listener
        storyAdapter.addLoadStateListener { loadState ->
            if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && storyAdapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {
                // List empty or error
                mainBinding.apply {
                    textError.animateVisibility(true)
                    textError.animateVisibility(true)
                    rvStories.animateVisibility(false)
                }
            } else {
                // List not empty
                mainBinding.apply {
                   textError.animateVisibility(false)
                    textError.animateVisibility(false)
                    rvStories.animateVisibility(true)
                }
            }

            // SwipeRefresh status based on LoadState
            mainBinding.swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading
        }

        try {
            recyclerView = mainBinding.rvStories
            recyclerView.apply {
                adapter = storyAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        storyAdapter.retry()
                    }
                )
                layoutManager = linearLayoutManager
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
    private fun updateRecyclerViewData(stories: PagingData<Story>) {
        // SaveInstanceState of recyclerview before add new data
        // It's prevent the recyclerview to scroll again to the top when load new data
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()

        // Add data to the adapter
        storyAdapter.submitData(lifecycle, stories)

        // Restore last recyclerview state
        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }
}