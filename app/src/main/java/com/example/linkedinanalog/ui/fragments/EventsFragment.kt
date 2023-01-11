package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.linkedinanalog.R
import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.Coordinates
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.data.models.user.UserModel
import com.example.linkedinanalog.databinding.FragmentEventsBinding
import com.example.linkedinanalog.ui.recyclerAdapters.eventAdapter.EventAdapter
import com.example.linkedinanalog.viewModels.EventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventsFragment : Fragment() {
    private lateinit var binding: FragmentEventsBinding
    private val adapter: EventAdapter = EventAdapter()
    private val viewModel: EventViewModel by activityViewModels()






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding.inflate(layoutInflater, container, false)
        binding.recyclerEvent.adapter = adapter


        viewModel.liveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getEvents()
        super.onViewCreated(view, savedInstanceState)
    }


}