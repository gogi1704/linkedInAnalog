package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.linkedinanalog.R
import com.example.linkedinanalog.auth.AuthState
import com.example.linkedinanalog.databinding.FragmentMyProfileBinding
import com.example.linkedinanalog.ui.extensions.loadAvatar
import com.example.linkedinanalog.ui.recyclerAdapters.jobAdapter.JobAdapter
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_KEY
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_VALUE_REG
import com.example.linkedinanalog.viewModels.AuthViewModel.Companion.AUTH_BUNDLE_VALUE_SIGN_IN
import com.example.linkedinanalog.viewModels.JobViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : Fragment() {

    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var adapter: JobAdapter
    private val jobViewModel: JobViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(layoutInflater, container, false)
        adapter = JobAdapter()
        binding.recyclerJob.adapter = adapter

        with(binding) {


            showAuthMenu(authViewModel.isAuth)
        }



        authViewModel.liveData.observe(viewLifecycleOwner) {
            //TODO
        }

        jobViewModel.liveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }






        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        jobViewModel.getAllJobs()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showAuthMenu(isAuth: Boolean) {
        if (!isAuth) {
            PopupMenu(requireContext(), binding.menuButton).apply {
                inflate(R.menu.auth_false_menu)
                setOnMenuItemClickListener { itemView ->
                    when (itemView.itemId) {
                        R.id.sign_in -> {
                            findNavController().navigate(
                                R.id.action_homeFragment_to_authFragment,
                                Bundle().apply {
                                    putString(
                                        AUTH_BUNDLE_KEY,
                                        AUTH_BUNDLE_VALUE_SIGN_IN
                                    )
                                })
                            true
                        }
                        R.id.registration -> {
                            findNavController().navigate(
                                R.id.action_homeFragment_to_authFragment,
                                Bundle().apply {
                                    putString(AUTH_BUNDLE_KEY, AUTH_BUNDLE_VALUE_REG)
                                })
                            true
                        }
                        else -> {
                            false
                        }
                    }

                }
            }.show()

        } else {
            PopupMenu(requireContext(), binding.menuButton).apply {
                inflate(R.menu.auth_true_menu)
                setOnMenuItemClickListener { itemView ->
                    when (itemView.itemId) {
                        R.id.sign_out -> {
                            authViewModel.signOut()
                            true
                        }
                        else -> {
                            false
                        }
                    }


                }
            }.show()
        }
    }


}