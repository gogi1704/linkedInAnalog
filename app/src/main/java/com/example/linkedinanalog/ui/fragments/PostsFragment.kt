package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.databinding.FragmentPostsBinding
import com.example.linkedinanalog.ui.recyclerAdapters.postAdapter.PostAdapter
import com.example.linkedinanalog.viewModels.PostViewModel
import kotlinx.coroutines.flow.collectLatest


class PostsFragment : Fragment() {

    private lateinit var binding: FragmentPostsBinding
    private lateinit var adapter: PostAdapter
    private val viewModel: PostViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(layoutInflater, container, false)
        adapter = PostAdapter()
        binding.recyclerPost.adapter = adapter

        binding.fbCreatePost.setOnClickListener {

        }

//        viewModel.liveData.observe(viewLifecycleOwner) {
//            adapter.submitList(it)
//        }


        lifecycleScope.launchWhenCreated {
            viewModel.pagingData.collectLatest {
                adapter.submitData(it)
            }

        }






        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getAllPosts()
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