package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.linkedinanalog.R
import com.example.linkedinanalog.data.models.Coordinates
import com.example.linkedinanalog.data.models.post.PostModel
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
        super.onViewCreated(view, savedInstanceState)
    }


}