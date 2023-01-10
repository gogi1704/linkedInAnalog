package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.linkedinanalog.R
import com.example.linkedinanalog.data.models.Coordinates
import com.example.linkedinanalog.data.models.post.PostModel
import com.example.linkedinanalog.databinding.FragmentPostsBinding
import com.example.linkedinanalog.ui.recyclerAdapters.postAdapter.PostAdapter

class PostsFragment : Fragment() {
    private val list = listOf(
        PostModel(
            1,
            1,
            "VASYA",
            null,
            "GAZPROM",
            "sdadad",
            "2022.21.21",
            Coordinates("2", "@"),
            "https://kotlinlang.ru/docs/enum-classes.html",
            listOf(),
            listOf(),
            true,
            true,
            null,
            true,
            listOf()
        ),
        PostModel(
            1,
            1,
            "VASYA",
            null,
            "GAZPROM",
            "sdadad",
            "2022.21.21",
            Coordinates("2", "@"),
            "https://kotlinlang.ru/docs/enum-classes.html",
            listOf(),
            listOf(),
            true,
            true,
            null,
            true,
            listOf()
        ),
        PostModel(
            1,
            1,
            "VASYA",
            null,
            "GAZPROM",
            "sdadad",
            "2022.21.21",
            Coordinates("2", "@"),
            "https://kotlinlang.ru/docs/enum-classes.html",
            listOf(),
            listOf(),
            true,
            true,
            null,
            true,
            listOf()
        )
    )

    private lateinit var binding: FragmentPostsBinding
    private lateinit var adapter: PostAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(layoutInflater, container, false)
        adapter = PostAdapter()

        binding.recyclerPost.adapter = adapter
        adapter.submitList(list)

        return binding.root
    }


}