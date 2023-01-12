package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.system.Os.remove
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.example.linkedinanalog.R
import com.example.linkedinanalog.databinding.FragmentMyProfileBinding
import com.example.linkedinanalog.ui.recyclerAdapters.jobAdapter.JobAdapter
import com.example.linkedinanalog.viewModels.JobViewModel

class MyProfileFragment : Fragment() {

    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var adapter: JobAdapter
    private val viewModel: JobViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(layoutInflater, container, false)
        adapter = JobAdapter()
        binding.recyclerJob.adapter = adapter

        binding.authButton.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.auth_false_menu)
                setOnMenuItemClickListener { itemView ->
                    when (itemView.itemId) {
                        R.id.sign_in -> {

                        }
                        R.id.registration -> {
                        }
                    }
                    false
                }
            }.show()
        }


        viewModel.liveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }






        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getAllJobs()
        super.onViewCreated(view, savedInstanceState)
    }


}