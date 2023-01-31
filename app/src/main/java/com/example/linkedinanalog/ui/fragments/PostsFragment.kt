package com.example.linkedinanalog.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.linkedinanalog.R
import com.example.linkedinanalog.data.models.post.PostCreateRequest
import com.example.linkedinanalog.databinding.FragmentPostsBinding
import com.example.linkedinanalog.exceptions.JobErrorType
import com.example.linkedinanalog.exceptions.PostErrorType
import com.example.linkedinanalog.ui.constans.*
import com.example.linkedinanalog.ui.recyclerAdapters.postAdapter.PostAdapter
import com.example.linkedinanalog.ui.recyclerAdapters.postAdapter.PostAdapterListener
import com.example.linkedinanalog.ui.recyclerAdapters.postAdapter.PostsLoadStateAdapter
import com.example.linkedinanalog.viewModels.AuthViewModel
import com.example.linkedinanalog.viewModels.PostViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PostsFragment : Fragment() {
    private lateinit var binding: FragmentPostsBinding
    private lateinit var adapter: PostAdapter
    private val authViewModel: AuthViewModel by activityViewModels()
    private val postViewModel: PostViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(layoutInflater, container, false)

        adapter = PostAdapter(object : PostAdapterListener {
            override fun deletePost(id: Long) {
                postViewModel.deletePost(id)
            }

            override fun likePost(id: Long, likeByMe: Boolean) {
                postViewModel.like(id, likeByMe)
            }

            override fun updatePost(post: PostCreateRequest) {
                findNavController().navigate(
                    R.id.action_homeFragment_to_createFragment,
                    Bundle().apply {
                        putString(OPEN_FRAGMENT_KEY, POST_OPEN)
                        putString(JOB_KEY, UPDATE)
                        putString(ITEM_KEY, Gson().toJson(post))
                    })
            }

            override fun showUser(id: Long) {
                requireContext().getSharedPreferences(USER_ID_PREFS, Context.MODE_PRIVATE)
                    .edit().putLong(SHOW_USER_KEY, id)
                    .apply()
                findNavController().navigate(
                    R.id.action_homeFragment_to_showUserFragment,
                    Bundle().apply {
                        putLong(SHOW_USER_KEY, id)
                    })

            }
        })
        binding.recyclerPost.adapter = adapter.withLoadStateHeader(
            header =
            PostsLoadStateAdapter()
        )



        with(binding) {
            fbCreatePost.setOnClickListener {
                if (authViewModel.isAuth) {
                    findNavController().navigate(
                        R.id.action_homeFragment_to_createFragment,
                        Bundle().apply {
                            putString(OPEN_FRAGMENT_KEY, POST_OPEN)
                            putString(JOB_KEY, CREATE)
                        })
                } else {
                    alertDialogShow()
                }

            }
            newPostsContainer.setOnClickListener {
                recyclerPost.smoothScrollToPosition(0)
                newPostsContainer.visibility = View.GONE
            }
        }


        binding.swiperefresh.setOnRefreshListener {
            adapter.refresh()
        }


        lifecycleScope.launchWhenCreated {
            postViewModel.pagingData.collectLatest {
                adapter.submitData(it)
            }
            adapter.refresh()
        }

        postViewModel.newerCount.observe(viewLifecycleOwner) {
            //todo
//            if (it != 0) {
//                binding.newPostsContainer.visibility = View.VISIBLE
//                binding.newPostsCount.text = it.toString()
//            }
//            adapter.refresh()
        }

        authViewModel.authLiveData.observe(viewLifecycleOwner) {
            adapter.refresh()
        }

        postViewModel.postErrorStateLiveData.observe(viewLifecycleOwner) {
            when (it.errorType) {
                PostErrorType.DeletePostError -> {
                    showToast("Delete error. Please try later.")
                }
                PostErrorType.LikePostError -> {
                    showToast("Like error. Please try later.")
                }
                PostErrorType.NetworkError ->{
                    showToast("Check internet connection and repeat")
                }
                else -> {}
            }
        }




        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { state ->
                binding.swiperefresh.isRefreshing =
                    state.refresh is LoadState.Loading ||
                            state.prepend is LoadState.Loading ||
                            state.append is LoadState.Loading

            }

        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
    private fun alertDialogShow() {
        AlertDialog.Builder(requireContext())
            .setTitle("Authentication error")
            .setMessage("Sign in to create events")
            .setPositiveButton(
                "Sign in"
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
            .setNegativeButton("Register") { _, _ ->
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