package com.example.linkedinanalog.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.linkedinanalog.R
import com.example.linkedinanalog.data.media.MediaLifecycleObserver
import com.example.linkedinanalog.data.media.mediaModels.PlayState
import com.example.linkedinanalog.databinding.FragmentEventsBinding
import com.example.linkedinanalog.exceptions.EventErrorType
import com.example.linkedinanalog.ui.constans.*
import com.example.linkedinanalog.ui.recyclerAdapters.eventAdapter.EventAdapter
import com.example.linkedinanalog.ui.recyclerAdapters.eventAdapter.EventListener
import com.example.linkedinanalog.ui.recyclerAdapters.userAdapter.UserAdapter
import com.example.linkedinanalog.viewModels.AudioViewModel
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EventsFragment : Fragment() {
    private lateinit var binding: FragmentEventsBinding
    private lateinit var eventAdapter: EventAdapter
    private lateinit var userAdapter: UserAdapter
    private val mediaObserver = MediaLifecycleObserver()
    private val audioViewModel: AudioViewModel by activityViewModels()
    private val eventViewModel: EventViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        authViewModel.getAllUsers()
        binding = FragmentEventsBinding.inflate(layoutInflater, container, false)
        eventAdapter = EventAdapter(object : EventListener {

            override fun showSpeakers(listId: List<Int>) {
                binding.usersShowGroup.visibility = View.VISIBLE
                authViewModel.getUsersList(listId)
            }

            override fun showParticipants(listId: List<Int>) {
                binding.usersShowGroup.visibility = View.VISIBLE
                authViewModel.getUsersList(listId)
            }

            override fun participateByMe(id: Long, isParticipatedByMe: Boolean) {
                if (authViewModel.isAuth) {
                    eventViewModel.participantByMe(id, isParticipatedByMe)
                } else {
                    alertDialogShow()
                }
            }

            override fun showUser(userId: Long) {
                requireContext().getSharedPreferences(USER_ID_PREFS, Context.MODE_PRIVATE)
                    .edit().putLong(SHOW_USER_KEY, userId)
                    .apply()
                findNavController().navigate(
                    R.id.action_homeFragment_to_showUserFragment,
                    Bundle().apply {
                        putLong(SHOW_USER_KEY, id.toLong())
                    })
            }

            override fun deleteEvent(id: Long) {
                eventViewModel.deleteEvent(id)
            }

            override fun playAudio(url: String?) {
                if (mediaObserver.musicNow == url) {
                    if (mediaObserver.isPlayed) {
                        mediaObserver.mediaPlayer?.pause()
                        mediaObserver.isPlayed = false
                        audioViewModel.playState = PlayState(isPlay = false, nameTrack = url)

                    } else {
                        mediaObserver.mediaPlayer?.start()
                        mediaObserver.isPlayed = true
                        audioViewModel.playState = PlayState(isPlay = true, nameTrack = url)
                    }
                } else {
                    mediaObserver.mediaPlayer?.reset()
                    mediaObserver.apply {
                        mediaPlayer?.setDataSource(url)
                    }.play(url!!)
                    mediaObserver.isPlayed = true
                    mediaObserver.musicNow = url
                    audioViewModel.playState = PlayState(isPlay = true, nameTrack = url)
                }
            }

        }, MediaController(requireActivity()))
        userAdapter = UserAdapter(null)

        binding.recyclerEvent.adapter = eventAdapter
        binding.participantsRecycler.adapter = userAdapter

        with(binding) {
            fbCreateEvent.setOnClickListener {
                if (authViewModel.isAuth) {
                    findNavController().navigate(
                        R.id.action_homeFragment_to_createFragment,
                        Bundle().apply {
                            putString(OPEN_FRAGMENT_KEY, EVENT_OPEN)
                            putString(JOB_KEY, CREATE)
                        })
                } else {
                    alertDialogShow()
                }
            }

            buttonClose.setOnClickListener {
                usersShowGroup.visibility = View.GONE
            }

        }


        lifecycleScope.launchWhenCreated {
            eventViewModel.pagingData.collectLatest {
                eventAdapter.submitData(it)
            }
        }

        eventViewModel.eventErrorStateLiveData.observe(viewLifecycleOwner) {

            when (it.errorType) {
                EventErrorType.ParticipantError -> {
                    makeToast(getString(R.string.Error_retry))
                    eventAdapter.refresh()
                }
                EventErrorType.NetworkError -> {
                    makeToast(getString(R.string.CheckInternet))
                }
                else -> {}
            }
        }

        authViewModel.participantsOrSpeakerLiveData.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.emptyList.visibility = View.VISIBLE
            } else binding.emptyList.visibility = View.GONE
            userAdapter.submitList(it)
        }

        authViewModel.authLiveData.observe(viewLifecycleOwner) {
            eventAdapter.refresh()
        }


        return binding.root
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun alertDialogShow() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.Authentication_error)
            .setMessage(getString(R.string.SignToCreate))
            .setPositiveButton(
                getString(R.string.SignIn)
            ) { _, _ ->
                findNavController().navigate(
                    R.id.action_homeFragment_to_authFragment,
                    Bundle().apply {
                        putString(
                            AuthViewModel.AUTH_BUNDLE_KEY,
                            AuthViewModel.AUTH_BUNDLE_VALUE_SIGN_IN
                        )
                    })
            }
            .setNegativeButton(getString(R.string.Registration)) { _, _ ->
                findNavController().navigate(
                    R.id.action_homeFragment_to_authFragment,
                    Bundle().apply {
                        putString(
                            AuthViewModel.AUTH_BUNDLE_KEY,
                            AuthViewModel.AUTH_BUNDLE_VALUE_REG
                        )
                    })
            }.show()
    }
}