package com.example.linkedinanalog.ui.recyclerAdapters.postAdapter

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.appcompat.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.R
import com.example.linkedinanalog.data.models.AttachmentType
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.databinding.RecyclerPostItemBinding
import com.example.linkedinanalog.ui.extensions.loadAvatar
import com.example.linkedinanalog.ui.extensions.loadImage
import com.example.linkedinanalog.ui.extensions.parseDateTime

interface PostAdapterListener {
    fun deletePost(id: Long)
    fun likePost(id: Long, likeByMe: Boolean)
    fun updatePost(post: PostCreateRequest)
    fun showUser(id: Long)
    fun playAudio(url: String?)

}


class PostAdapter(
    private val listener: PostAdapterListener,
    private val mediaController: MediaController
) :
    PagingDataAdapter<PostModel, PostAdapter.PostViewHolder>(PostDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            RecyclerPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, listener, mediaController)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class PostViewHolder(
        private val binding: RecyclerPostItemBinding,
        private val listener: PostAdapterListener,
        private val mediaController: MediaController,
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

                if (item.ownedByMe) {
                    buttonMenu.visibility = View.VISIBLE
                } else buttonMenu.visibility = View.GONE

                if (item.attachment != null) {
                    when (item.attachment.type) {
                        AttachmentType.IMAGE -> {
                            attachmentImage.visibility = View.VISIBLE
                            audioGroup.visibility = View.GONE
                            videoGroup.visibility = View.GONE
                            attachmentImage.loadImage(item.attachment.url.toString())
                        }
                        AttachmentType.AUDIO -> {
                            audioGroup.visibility = View.VISIBLE
                            attachmentImage.visibility = View.GONE
                            videoGroup.visibility = View.GONE

                            textNameMusic.text = item.attachment.url.toString()

                            playAudio.setOnClickListener {
                                listener.playAudio(item.attachment.url)
                            }

                        }
                        AttachmentType.VIDEO -> {
                            videoGroup.visibility = View.VISIBLE
                            audioGroup.visibility = View.GONE
                            attachmentImage.visibility = View.GONE
                            videoView.apply {
                                setMediaController(mediaController)
                                setVideoURI(Uri.parse(item.attachment.url))
                                setOnCompletionListener {
                                    stopPlayback()
                                }
                                videoPlay.setOnClickListener {
                                    videoView.start()
                                }
                                videoStop.setOnClickListener {
                                    videoView.pause()
                                }
                            }

                        }
                        else -> {}
                    }

                } else {
                    audioGroup.visibility = View.GONE
                    videoGroup.visibility = View.GONE
                    attachmentImage.visibility = View.GONE
                }

                buttonLike.isChecked = item.likedByMe
                buttonLike.text =
                    if (item.likeOwnerIds?.isEmpty() == true) "0"
                    else item.likeOwnerIds?.size.toString()



                buttonLike.setOnClickListener {
                    listener.likePost(item.id.toLong(), item.likedByMe)
                }

                imageAvatar.setOnClickListener {
                    listener.showUser(item.authorId.toLong())
                }


                buttonMenu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.bottom_menu)
                        setOnMenuItemClickListener { itemView ->
                            when (itemView.itemId) {
                                R.id.Update -> {
                                    listener.updatePost(item.toPostCreateRequest())
                                    return@setOnMenuItemClickListener true
                                }

                                R.id.Delete -> {
                                    listener.deletePost(item.id.toLong())
                                    return@setOnMenuItemClickListener true
                                }
                                else -> {
                                    false
                                }
                            }

                        }
                    }.show()
                }
            }
        }

    }
}