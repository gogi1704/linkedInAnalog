package com.example.linkedinanalog.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.linkedinanalog.data.repository.WallRepository
import com.example.linkedinanalog.databinding.FragmentShowUserBinding
import com.example.linkedinanalog.ui.constans.SHOW_USER_KEY
import com.example.linkedinanalog.ui.constans.USER_ID_PREFS
import com.example.linkedinanalog.ui.extensions.loadAvatar
import com.example.linkedinanalog.ui.recyclerAdapters.jobAdapter.JobAdapter
import com.example.linkedinanalog.ui.recyclerAdapters.wallAdapter.WallAdapter
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.JobViewModel
import com.example.linkedinanalog.viewModels.PostViewModel
import com.example.linkedinanalog.viewModels.WallViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ShowUserFragment : Fragment() {
    private lateinit var binding: FragmentShowUserBinding
    private lateinit var adapter: WallAdapter
    private val authViewModel: AuthViewModel by activityViewModels()
    private val wallViewModel: WallViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        wallViewModel.removeAll()
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowUserBinding.inflate(layoutInflater, container, false)
        adapter = WallAdapter(object : WallAdapter.WallAdapterListener {
            override fun likePost(id: Long, likedByMe: Boolean) {
                wallViewModel.like(id, likedByMe)
            }

        })
        binding.recyclerUserWall.adapter = adapter
        authViewModel.getUserById(requireArguments().getLong(SHOW_USER_KEY))
        //  wallViewModel.getAll(requireArguments().getLong(SHOW_USER_KEY))


        authViewModel.showUserLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                textUserName.text = it.name
                imageAvatar.loadAvatar(it.avatar ?: "")
            }
        }

//        wallViewModel.wallLiveData.observe(viewLifecycleOwner) {
//            //adapter.submitList(it)
//        }

//        wallViewModel.wallDataFlow.observe(viewLifecycleOwner){
//            adapter.submitList(it)
////        }

        lifecycleScope.launchWhenCreated {
            wallViewModel.pagingData.collectLatest {
                adapter.submitData(it)

            }
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.refresh()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        requireContext().applicationContext.getSharedPreferences(
            USER_ID_PREFS,
            Context.MODE_PRIVATE
        )
            .edit()
            .clear()
            .apply()
        super.onPause()
    }
}