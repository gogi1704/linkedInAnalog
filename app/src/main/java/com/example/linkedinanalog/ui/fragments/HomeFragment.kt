package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.linkedinanalog.R
import com.example.linkedinanalog.databinding.FragmentHomeBinding
import com.example.linkedinanalog.ui.pagerAdapter.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var viewPagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager2
    private val fragments = listOf(PostsFragment(), EventsFragment(), MyProfileFragment())
    private val fragmentsNames = listOf("Posts", "Events", "My profile")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)


        viewPager = binding.viewPager
        viewPagerAdapter = PagerAdapter(this, fragments)
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabBar, viewPager) { tab, position ->
            tab.text = fragmentsNames[position]
        }.attach()

        return binding.root
    }

}