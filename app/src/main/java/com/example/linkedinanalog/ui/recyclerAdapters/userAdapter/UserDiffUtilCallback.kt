package com.example.linkedinanalog.ui.recyclerAdapters.userAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.data.models.user.UserModel

class UserDiffUtilCallback : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem == newItem
    }
}