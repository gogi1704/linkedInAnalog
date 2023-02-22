package com.example.linkedinanalog.ui.recyclerAdapters.wallAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.databinding.RecyclerPostItemBinding
import com.example.linkedinanalog.ui.extensions.loadAvatar
import com.example.linkedinanalog.ui.extensions.loadImage
import com.example.linkedinanalog.ui.extensions.parseDateTime
import com.example.linkedinanalog.ui.recyclerAdapters.postAdapter.PostDiffUtilCallback

class WallAdapter(private val listener: WallAdapterListener) :
    PagingDataAdapter<PostModel, WallAdapter.WallViewHolder>(PostDiffUtilCallback()) {

    interface WallAdapterListener {
        fun likePost(id: Long, likedByMe: Boolean)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallViewHolder {
        val binding =
            RecyclerPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WallViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: WallViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class WallViewHolder(
        private val binding: RecyclerPostItemBinding,
        private val listener: WallAdapterListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostModel) {

            with(binding) {
                textAuthorName.text = item.author
                textAuthorJob.text = item.authorJob
                imageAvatar.loadAvatar(item.authorAvatar.toString())
                published.parseDateTime(item.published)
                content.text = item.content
                if (item.link == null) {
                    groupLink.visibility = View.GONE
                } else {
                    groupLink.visibility = View.VISIBLE
                    link.text = item.link
                }

                buttonMenu.visibility = View.GONE

                if (item.attachment != null) {
                    attachmentImage.visibility = View.VISIBLE
                    attachmentImage.loadImage(item.attachment.url.toString())
                } else attachmentImage.visibility = View.GONE

                buttonLike.isChecked = item.likedByMe
                buttonLike.text =
                    if (item.likeOwnerIds?.size == null) "0" else item.likeOwnerIds.size.toString()

                buttonLike.setOnClickListener {
                    listener.likePost(item.id.toLong(), item.likedByMe)
                }


            }
        }

    }
}