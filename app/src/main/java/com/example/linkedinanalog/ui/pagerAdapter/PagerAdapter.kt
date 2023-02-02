package com.example.linkedinanalog.ui.pagerAdapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragment: Fragment, private val  listFragments: List<Fragment>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = listFragments.size

    override fun createFragment(position: Int): Fragment {
       return listFragments[position]
    }
}