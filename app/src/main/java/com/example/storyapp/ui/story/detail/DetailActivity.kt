package com.example.storyapp.ui.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.data.model.Story
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.util.ext.setImageUrl
import com.example.storyapp.R.string
import com.example.storyapp.util.Const


class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var story: Story

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
    initIntent()
        initUI()
    }


    private fun initUI() {
        detailBinding.apply {
            imgStoryThumbnail.setImageUrl(story.photoUrl, true)
            tvStoryTitle.text = story.name
            tvStoryDesc.text = story.description
        }
        title = getString(string.title_detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initIntent() {
        story = intent.getParcelableExtra(Const.BUNDLE_KEY_STORY)!!
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }

}