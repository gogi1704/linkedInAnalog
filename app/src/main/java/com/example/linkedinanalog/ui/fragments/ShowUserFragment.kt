package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.linkedinanalog.databinding.FragmentShowUserBinding
import com.example.linkedinanalog.ui.constans.SHOW_USER_KEY
import com.example.linkedinanalog.ui.extensions.loadAvatar
import com.example.linkedinanalog.ui.recyclerAdapters.jobAdapter.JobAdapter
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.JobViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowUserFragment : Fragment() {
    private lateinit var binding: FragmentShowUserBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private val jobViewModel: JobViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowUserBinding.inflate(layoutInflater, container, false)
        val adapter = JobAdapter()
        authViewModel.getUserById(requireArguments().getLong(SHOW_USER_KEY))
        jobViewModel.getJobById(requireArguments().getLong(SHOW_USER_KEY))




        authViewModel.showUserLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                textUserName.text = it.name
                imageAvatar.loadAvatar(it.avatar ?: "")
            }
        }

        jobViewModel.userShowJobLiveData.observe(viewLifecycleOwner) {
            binding.recyclerJob.adapter = adapter
            adapter.submitList(it)
        }


        return binding.root
    }


}