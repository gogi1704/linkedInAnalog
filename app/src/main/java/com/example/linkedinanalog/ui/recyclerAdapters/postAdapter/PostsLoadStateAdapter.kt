package com.example.linkedinanalog.ui.recyclerAdapters.postAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.databinding.LoadStateLayoutBinding

class PostsLoadStateAdapter(private val onInteractionListener: OnInteractionListener) :
    LoadStateAdapter<PostsLoadStateAdapter.PostsLoadStateViewHolder>() {

    interface OnInteractionListener {
        fun onRetry() {}
    }

    override fun onBindViewHolder(holder: PostsLoadStateViewHolder, loadState: LoadState) {
       holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PostsLoadStateViewHolder {
        val binding = LoadStateLayoutBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return  PostsLoadStateViewHolder(binding , onInteractionListener)
    }


    class PostsLoadStateViewHolder(
        private val binding: LoadStateLayoutBinding,
        private val listener: OnInteractionListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.apply {
                progress.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState is LoadState.Error
                buttonRetry.setOnClickListener {
                    listener.onRetry()
                }
            }

        }


    }

}