package com.example.linkedinanalog.ui.recyclerAdapters.jobAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.linkedinanalog.data.models.job.JobModel

class JobDiffUtilCallback : DiffUtil.ItemCallback<JobModel>() {
    override fun areItemsTheSame(oldItem: JobModel, newItem: JobModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: JobModel, newItem: JobModel): Boolean {
        return oldItem == newItem
    }
}