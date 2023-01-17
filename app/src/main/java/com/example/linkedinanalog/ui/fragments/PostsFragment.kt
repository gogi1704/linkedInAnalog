package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.R
import com.example.linkedinanalog.databinding.FragmentPostsBinding
import com.example.linkedinanalog.ui.extensions.loadAvatar
import com.example.linkedinanalog.ui.recyclerAdapters.postAdapter.PostAdapter
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PostsFragment : Fragment() {


    private lateinit var binding: FragmentPostsBinding
    private lateinit var adapter: PostAdapter
    private val postViewModel: PostViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(layoutInflater, container, false)
        adapter = PostAdapter()
        binding.recyclerPost.adapter = adapter

        binding.fbCreatePost.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createFragment , Bundle().apply {
                putString("OPEN_FRAGMENT_KEY" , "POST")
            })
        }

//        viewModel.liveData.observe(viewLifecycleOwner) {
//            adapter.submitList(it)
//        }


        lifecycleScope.launchWhenCreated {
            postViewModel.pagingData.collectLatest {
                adapter.submitData(it)
            }

        }

        authViewModel.userLiveData.observe(viewLifecycleOwner) {

        }






        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postViewModel.getAllPosts()
        with(binding) {
            recyclerPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && fbCreatePost.isShown) {
                        fbCreatePost.hide()
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        fbCreatePost.show()
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
        super.onViewCreated(view, savedInstanceState)
    }


}