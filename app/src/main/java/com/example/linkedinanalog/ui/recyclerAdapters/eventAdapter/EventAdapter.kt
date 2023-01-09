package com.example.linkedinanalog.ui.recyclerAdapters.eventAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.databinding.RecyclerEventItemBinding

class EventAdapter :
    ListAdapter<EventModel, EventAdapter.EventViewHolder>(EventDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            RecyclerEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class EventViewHolder(private val binding: RecyclerEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EventModel) {
            with(binding) {
                textAuthorName.text = item.author
                textAuthorJob.text = item.authorJob
                eventStart.text = item.dateTime
                eventType.text = item.type
                published.text = item.published
                content.text = item.content
                link.text = item.link ?: ""
            }
        }
    }
}