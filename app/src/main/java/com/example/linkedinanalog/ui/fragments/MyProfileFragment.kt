package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.linkedinanalog.data.models.job.JobModel
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