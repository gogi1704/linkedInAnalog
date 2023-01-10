package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.linkedinanalog.R
import com.example.linkedinanalog.data.models.Attachment
import com.example.linkedinanalog.data.models.Coordinates
import com.example.linkedinanalog.data.models.event.EventModel
import com.example.linkedinanalog.databinding.FragmentEventsBinding
import com.example.linkedinanalog.ui.recyclerAdapters.eventAdapter.EventAdapter


class EventsFragment : Fragment() {
    private lateinit var binding: FragmentEventsBinding
    private val adapter: EventAdapter = EventAdapter()

    private val list = listOf(
        EventModel(
            id = 1,
            authorId = 1,
            author = "qwqw",
            authorAvatar = "https://ik.imagekit.io/jwudrxfj5ek/53e96c88-0796-491d-b98a-07542d532443..png___IdaKUqT3b",
            authorJob = "ooo",
            content = "dasdadas",
            dateTime = "20/22/22",
            published = "12/12/12",
            coords = Coordinates("", ""),
            type = "",
            likeOwnerIds = listOf(),
            likedByMe = true,
            speakerIds = listOf(),
            participantsIds = listOf(),
            participatedByMe = true,
            attachment = null,
            link = "https://netomedia.ru/swagger/",
            ownedByMe = true,
            users = listOf()
        ),
        EventModel(
            id = 13,
            authorId = 1,
            author = "qwqw",
            authorAvatar = "",
            authorJob = "ooo",
            content = "dasdadas",
            dateTime = "20/22/22",
            published = "12/12/12",
            coords = Coordinates("", ""),
            type = "",
            likeOwnerIds = listOf(),
            likedByMe = true,
            speakerIds = listOf(),
            participantsIds = listOf(),
            participatedByMe = true,
            attachment = null,
            link = "https://netomedia.ru/swagger/",
            ownedByMe = true,
            users = listOf()
        ),
        EventModel(
            id = 2,
            authorId = 1,
            author = "qwqw",
            authorAvatar = "https://ik.imagekit.io/jwudrxfj5ek/53e96c88-0796-491d-b98a-07542d532443..png___IdaKUqT3b",
            authorJob = "ooo",
            content = "dasdadas",
            dateTime = "20/22/22",
            published = "12/12/12",
            coords = Coordinates("", ""),
            type = "",
            likeOwnerIds = listOf(),
            likedByMe = true,
            speakerIds = listOf(),
            participantsIds = listOf(),
            participatedByMe = true,
            attachment = null,
            link = "https://netomedia.ru/swagger/",
            ownedByMe = true,
            users = listOf()
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding.inflate(layoutInflater, container, false)
        binding.recyclerEvent.adapter = adapter
        adapter.submitList(list)






        return binding.root
    }


}