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
import com.example.linkedinanalog.ui.constans.OPEN_FRAGMENT_KEY
import com.example.linkedinanalog.ui.constans.POST_OPEN
import com.example.linkedinanalog.ui.extensions.loadAvatar
import com.example.linkedinanalog.ui.recyclerAdapters.postAdapter.PostAdapter
import com.example.linkedinanalog.ui.recyclerAdapters.postAdapter.PostAdapterListener
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import okhttp3.internal.notify

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

        adapter = PostAdapter(object : PostAdapterListener{
            override fun deletePost(id:Long) {
               postViewModel.deletePost(id)
            }

            override fun updatePost() {
                TODO("Not yet implemented")
            }
        })

        binding.recyclerPost.adapter = adapter


        binding.fbCreatePost.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_createFragment,
                Bundle().apply {
                    putString(OPEN_FRAGMENT_KEY, POST_OPEN)
                })
        }




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