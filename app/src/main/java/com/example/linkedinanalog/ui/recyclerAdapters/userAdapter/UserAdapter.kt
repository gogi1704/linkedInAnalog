package com.example.linkedinanalog.ui.recyclerAdapters.userAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.linkedinanalog.data.models.user.UserModel
import com.example.linkedinanalog.databinding.RecyclerUserItemBinding
import com.example.linkedinanalog.ui.extensions.loadAvatar

interface UserListener {
    fun clickCheckBox(user: UserModel)
}

class UserAdapter(private val userListener: UserListener?) :
    ListAdapter<UserModel, UserAdapter.UserViewHolder>(UserDiffUtilCallback()) {


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            RecyclerUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, userListener)
    }


    class UserViewHolder(
        private val binding: RecyclerUserItemBinding,
        private val listener: UserListener?
    ) : ViewHolder(binding.root) {
        fun bind(item: UserModel?) {
            with(binding) {
                if (item != null) {
                    imageAvatar.loadAvatar(item.avatar ?: "")
                    textName.text = item.name
                    checkboxChooseUser.setOnClickListener {
                        listener?.clickCheckBox(item)
                    }
                }
                if (listener == null) {
                    checkboxChooseUser.visibility = View.GONE
                } else
                    checkboxChooseUser.visibility = View.VISIBLE

            }

        }
    }
}