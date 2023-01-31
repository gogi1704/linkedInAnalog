package com.example.linkedinanalog.ui.recyclerAdapters.eventAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.databinding.RecyclerEventItemBinding
import com.example.linkedinanalog.ui.extensions.loadAvatar
import com.example.linkedinanalog.ui.extensions.loadImage
import com.example.linkedinanalog.ui.extensions.parseDateTime
import com.example.linkedinanalog.ui.pagerAdapter.PagerAdapter

interface EventListener {
    fun showSpeakers(listId: List<Int>)
    fun showParticipants(listId: List<Int>)
    fun participateByMe(id: Long, isParticipatedByMe: Boolean)
    fun deleteEvent(id: Long)
}

class EventAdapter(private val listener: EventListener) :
    PagingDataAdapter<EventModel, EventAdapter.EventViewHolder>(EventDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            RecyclerEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    class EventViewHolder(
        private val binding: RecyclerEventItemBinding,
        private val listener: EventListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EventModel) {
            with(binding) {
                imageAvatar.loadAvatar(item.authorAvatar.toString())
                textAuthorName.text = item.author
                textAuthorJob.text = item.authorJob
                eventStart.text = item.datetime
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
                    attachmentImage.visibility = View.VISIBLE
                    attachmentImage.loadImage(item.attachment.url.toString())
                } else attachmentImage.visibility = View.GONE

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