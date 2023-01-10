package com.example.linkedinanalog.ui.recyclerAdapters.postAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.models.post.PostModel

class PostDiffUtilCallback: DiffUtil.ItemCallback<PostModel>() {
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem == newItem
    }
}