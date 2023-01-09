package com.example.linkedinanalog.ui.recyclerAdapters.eventAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.models.job.JobModel

class EventDiffUtilCallback : DiffUtil.ItemCallback<EventModel>() {
    override fun areItemsTheSame(oldItem: EventModel, newItem: EventModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EventModel, newItem: EventModel): Boolean {
        return oldItem == newItem
    }
}