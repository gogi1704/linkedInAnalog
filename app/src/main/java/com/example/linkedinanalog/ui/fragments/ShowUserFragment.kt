package com.example.linkedinanalog.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.linkedinanalog.databinding.FragmentShowUserBinding
import com.example.linkedinanalog.exceptions.AuthErrorType
import com.example.linkedinanalog.ui.constans.SHOW_USER_KEY
import com.example.linkedinanalog.ui.constans.USER_ID_PREFS
import com.example.linkedinanalog.ui.extensions.loadAvatar
import com.example.linkedinanalog.ui.recyclerAdapters.jobAdapter.JobAdapter
import com.example.linkedinanalog.ui.recyclerAdapters.wallAdapter.WallAdapter
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.JobViewModel
import com.example.linkedinanalog.viewModels.WallViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShowUserFragment : Fragment() {
    private lateinit var binding: FragmentShowUserBinding
    private lateinit var wallAdapter: WallAdapter
    private lateinit var jobAdapter: JobAdapter
    private val authViewModel: AuthViewModel by activityViewModels()
    private val wallViewModel: WallViewModel by activityViewModels()
    private val jobViewModel: JobViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        wallViewModel.removeAll()
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowUserBinding.inflate(layoutInflater, container, false)
        jobAdapter = JobAdapter()
        wallAdapter = WallAdapter(object : WallAdapter.WallAdapterListener {
            override fun likePost(id: Long, likedByMe: Boolean) {
                wallViewModel.like(id, likedByMe)
            }

        })
        binding.recyclerUserWall.adapter = wallAdapter
        binding.jobsRecycler.adapter = jobAdapter
        authViewModel.getUserById(requireArguments().getLong(SHOW_USER_KEY))


        authViewModel.showUserLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                textUserName.text = it.name
                imageAvatar.loadAvatar(it.avatar ?: "")

                buttonUserJobs.setOnClickListener {
                    jobViewModel.getJobById(requireArguments().getLong(SHOW_USER_KEY))
                    groupJobs.visibility = View.VISIBLE
                }

                buttonCloseJobs.setOnClickListener {
                    groupJobs.visibility = View.GONE
                }
            }

        }

        jobViewModel.userShowJobLiveData.observe(viewLifecycleOwner) {
            jobAdapter.submitList(it)
        }

        authViewModel.errorStateLiveData.observe(viewLifecycleOwner) {
            if (it.errorType == AuthErrorType.GetUserError) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Load error")
                    .setMessage("Load data of user error! Please retry")
                    .setPositiveButton("Retry") { _, _ ->
                        authViewModel.getUserById(requireArguments().getLong(SHOW_USER_KEY))
                    }.show()
            }
        }



        lifecycleScope.launchWhenCreated {
            wallViewModel.pagingData.collectLatest {
                wallAdapter.submitData(it)

            }
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        wallAdapter.refresh()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStop() {
        requireContext().applicationContext.getSharedPreferences(
            USER_ID_PREFS,
            Context.MODE_PRIVATE
        )
            .edit()
            .clear()
            .apply()
        super.onStop()
    }
}