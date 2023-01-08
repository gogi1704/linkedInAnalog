package com.example.linkedinanalog.ui.recyclerAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.databinding.RecyclerJobItemBinding


class JobAdapter : ListAdapter<JobModel, JobAdapter.JobViewHolder>(DiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = RecyclerJobItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class JobViewHolder(private val binding: RecyclerJobItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(job: JobModel) {
            with(binding) {
                jobName.text = job.name
                jobPosition.text = job.position
                jobStart.text = job.start
                jobFinish.text = job.finish ?: jobFinish.text
                link.text = job.link
            }
        }

    }
}