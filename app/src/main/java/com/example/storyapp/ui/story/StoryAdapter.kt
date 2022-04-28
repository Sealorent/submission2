package com.example.storyapp.ui.story

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.data.model.Story
import com.example.storyapp.databinding.ItemStoryRowBinding
import com.example.storyapp.ui.story.detail.DetailActivity
import com.example.storyapp.util.Const
import com.example.storyapp.util.ext.setImageUrl
import com.example.storyapp.util.ext.timeStamptoString

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.StoryViewHolder {
        val binding = ItemStoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        story?.let { holder.bind(it) }
    }


    class StoryViewHolder(private val binding: ItemStoryRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.apply {
                tvStoryTitle.text = story.name
                tvStoryDesc.text = story.description
                tvStoryDate.text = story.createdAt.timeStamptoString()

                imgStoryThumbnail.setImageUrl(story.photoUrl, true)
            }
            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java)
                intent.putExtra(Const.BUNDLE_KEY_STORY, story)

                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.imgStoryThumbnail, "thumbnail"),
                    Pair(binding.tvStoryTitle, "title"),
                    Pair(binding.tvStoryDesc, "description"),
                )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }



}