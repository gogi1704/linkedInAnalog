package com.example.linkedinanalog.ui.recyclerAdapters.eventAdapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.data.models.AttachmentType
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.databinding.RecyclerEventItemBinding
import com.example.linkedinanalog.ui.extensions.loadAvatar
import com.example.linkedinanalog.ui.extensions.loadImage
import com.example.linkedinanalog.ui.extensions.parseDate
import com.example.linkedinanalog.ui.extensions.parseDateTime

interface EventListener {
    fun showSpeakers(listId: List<Int>)
    fun showParticipants(listId: List<Int>)
    fun participateByMe(id: Long, isParticipatedByMe: Boolean)
    fun deleteEvent(id: Long)
    fun playAudio(url: String?)
}

class EventAdapter(
    private val listener: EventListener,
    private val mediaController: MediaController
) :
    PagingDataAdapter<EventModel, EventAdapter.EventViewHolder>(EventDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            RecyclerEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, listener , mediaController)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    class EventViewHolder(
        private val binding: RecyclerEventItemBinding,
        private val listener: EventListener,
        private val mediaController: MediaController
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EventModel) {
            with(binding) {
                imageAvatar.loadAvatar(item.authorAvatar.toString())
                textAuthorName.text = item.author
                textAuthorJob.text = item.authorJob
                eventStart.parseDate(item.datetime)
                eventType.text = item.type.toString()
                published.parseDateTime(item.published)
                content.text = item.content
                if (item.link == null) {
                    groupLink.visibility = View.GONE
                } else {
                    groupLink.visibility = View.VISIBLE
                    link.text = item.link
                }
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

                buttonSpeakers.setOnClickListener {
                    listener.showSpeakers(item.speakerIds ?: listOf())
                }

                buttonParticipants.setOnClickListener {
                    listener.showParticipants(item.participantsIds ?: listOf())
                }
                if (item.ownedByMe) {
                    deleteEvent.visibility = View.VISIBLE
                } else deleteEvent.visibility = View.GONE

                deleteEvent.setOnClickListener {
                    listener.deleteEvent(item.id.toLong())
                }
                checkBoxParticipatedByMe.isChecked = item.participatedByMe
                checkBoxParticipatedByMe.setOnClickListener {
                    listener.participateByMe(item.id.toLong(), item.participatedByMe)
                }
            }
        }
    }
}