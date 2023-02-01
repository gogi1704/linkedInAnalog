package com.example.linkedinanalog.ui.recyclerAdapters.jobAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.databinding.RecyclerJobItemBinding
import com.example.linkedinanalog.ui.extensions.parseDate


class JobAdapter(private val listener: JobListener?) :
    ListAdapter<JobModel, JobAdapter.JobViewHolder>(JobDiffUtilCallback()) {

    interface JobListener {
        fun deleteJob(id: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding =
            RecyclerJobItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class JobViewHolder(
        private val binding: RecyclerJobItemBinding,
        private val listener: JobListener?,
       // private val isMyJobs: Boolean?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(job: JobModel) {
            with(binding) {
                jobName.text = job.name
                jobPosition.text = job.position
                jobStart.parseDate(job.start)
                jobFinish.parseDate(job.finish)
                link.text = job.link
                if (listener != null) {
                    deleteJob.visibility = View.VISIBLE
                    deleteJob.setOnClickListener {
                        listener.deleteJob(job.id.toLong())
                    }
                } else deleteJob.visibility = View.GONE


            }
        }

    }
}