package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.linkedinanalog.data.models.job.JobModel
import com.example.linkedinanalog.databinding.FragmentMyProfileBinding
import com.example.linkedinanalog.ui.recyclerAdapters.jobAdapter.JobAdapter

class MyProfileFragment : Fragment() {
    val list = listOf(
        JobModel(1, "3", "wqwкee", "dasаdcv", "dasdaf", "Dsadas"),
        JobModel(2, "212", "wqwаee", "dвasdcv", "dasdaf", "Dsadas"),
        JobModel(3, "2", "wqwыee", "dasыdcv", "dasdaf", "Dsadas")
    )

    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var adapter: JobAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(layoutInflater, container, false)
        adapter = JobAdapter()
        with(binding) {
            recyclerJob.adapter = adapter
            adapter.submitList(list)
        }








        return binding.root
    }



}