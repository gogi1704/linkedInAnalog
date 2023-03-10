package com.example.linkedinanalog.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.linkedinanalog.R
import com.example.linkedinanalog.databinding.FragmentShowUserBinding
import com.example.linkedinanalog.exceptions.AuthErrorType
import com.example.linkedinanalog.exceptions.JobErrorType
import com.example.linkedinanalog.exceptions.WallErrorType
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
        val userId = requireParentFragment().context?.getSharedPreferences(
            USER_ID_PREFS,
            Context.MODE_PRIVATE
        )?.getLong(SHOW_USER_KEY, 0)
        binding = FragmentShowUserBinding.inflate(layoutInflater, container, false)
        jobAdapter = JobAdapter(null)
        wallAdapter = WallAdapter(object : WallAdapter.WallAdapterListener {
            override fun likePost(id: Long, likedByMe: Boolean) {
                if (authViewModel.isAuth) {
                    wallViewModel.like(id, likedByMe)
                } else alertDialogShow()

            }

        })
        binding.recyclerUserWall.adapter = wallAdapter
        binding.jobsRecycler.adapter = jobAdapter
        wallViewModel.getAll(userId!!)
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

        jobViewModel.jobErrorStateLiveData.observe(viewLifecycleOwner) {
            when (it.errorType) {
                JobErrorType.GetJobError -> {
                    showToast(getString(R.string.LoadDataError))
                }
                else -> {}
            }
        }

        jobViewModel.userShowJobLiveData.observe(viewLifecycleOwner) {
            jobAdapter.submitList(it)
        }

        authViewModel.errorStateLiveData.observe(viewLifecycleOwner) {
            when (it.errorType) {
                AuthErrorType.GetUserError -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.LoadError))
                        .setMessage(R.string.LoadDataError)
                        .setPositiveButton(getString(R.string.Retry)) { _, _ ->
                            authViewModel.getUserById(requireArguments().getLong(SHOW_USER_KEY))
                        }.show()
                }
                AuthErrorType.NetworkError -> {
                    showToast(getString(R.string.CheckInternet))
                }

                else -> {}
            }
        }

        wallViewModel.wallErrorStateLiveData.observe(viewLifecycleOwner) {
            when (it.errorType) {
                WallErrorType.WallLikeError -> {
                    showToast(getString(R.string.LikeError))
                }
                WallErrorType.NetworkError -> {
                    showToast(getString(R.string.CheckInternet))
                }
                else -> {}
            }
        }



        lifecycleScope.launchWhenCreated {
            wallViewModel.pagingData.collectLatest {
                wallAdapter.submitData(it)

            }
        }




        return binding.root
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

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun alertDialogShow() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.Authentication_error))
            .setMessage(getString(R.string.SignToCreate))
            .setPositiveButton(
                getString(R.string.SignIn)
            ) { _, _ ->
                findNavController().navigate(
                    R.id.action_showUserFragment_to_authFragment,
                    Bundle().apply {
                        putString(
                            AuthViewModel.AUTH_BUNDLE_KEY,
                            AuthViewModel.AUTH_BUNDLE_VALUE_SIGN_IN
                        )
                    })
            }
            .setNegativeButton(getString(R.string.Registration)) { _, _ ->
                findNavController().navigate(
                    R.id.action_showUserFragment_to_authFragment,
                    Bundle().apply {
                        putString(
                            AuthViewModel.AUTH_BUNDLE_KEY,
                            AuthViewModel.AUTH_BUNDLE_VALUE_REG
                        )
                    })
            }.show()
    }
}