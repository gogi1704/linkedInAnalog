package com.example.linkedinanalog.ui.fragments

import android.os.Binder
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.linkedinanalog.R
import com.example.linkedinanalog.databinding.ActivityMainBinding
import com.example.linkedinanalog.ui.pagerAdapter.PagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var viewPagerAdapter: PagerAdapter
//    private lateinit var viewPager: ViewPager2
//
//
//    private val fragments = listOf(PostsFragment(), EventsFragment(), MyProfileFragment())
//    private val fragmentsNames = listOf("Posts", "Events", "My profile")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        viewPager = binding.viewPager
//        viewPagerAdapter = PagerAdapter(this, fragments)
//        viewPager.adapter = viewPagerAdapter
//        TabLayoutMediator(binding.tabBar, viewPager) { tab, position ->
//            tab.text = fragmentsNames[position]
//        }.attach()

    }

}