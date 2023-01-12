package com.example.linkedinanalog.ui.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.linkedinanalog.databinding.ActivityMainBinding
import com.example.linkedinanalog.ui.pagerAdapter.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewPagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var binding:ActivityMainBinding
    private val fragments = listOf(PostsFragment(), EventsFragment(), MyProfileFragment())
    private val fragmentsNames = listOf("Posts", "Events", "My profile")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager = binding.viewPager
        viewPagerAdapter = PagerAdapter(this, fragments)
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabBar, viewPager) { tab, position ->
            tab.text = fragmentsNames[position]
        }.attach()

    }
}