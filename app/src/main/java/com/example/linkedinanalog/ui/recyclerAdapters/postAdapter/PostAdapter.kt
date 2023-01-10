package com.example.linkedinanalog.ui.recyclerAdapters.postAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.databinding.RecyclerPostItemBinding
import com.example.linkedinanalog.ui.extensions.loadAvatar

class PostAdapter : ListAdapter<PostModel, PostAdapter.PostViewHolder>(PostDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            RecyclerPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PostViewHolder(private val binding: RecyclerPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostModel) {
            with(binding) {
                textAuthorName.text = item.author
                textAuthorJob.text = item.authorJob
                imageAvatar.loadAvatar(item.authorAvatar.toString())
                published.text = item.published
                content.text = item.content
                if (item.link == null) {
                    groupLink.visibility = View.GONE
                } else {
                    groupLink.visibility = View.VISIBLE
                    link.text = item.link
                }

            }
        }

    }
}