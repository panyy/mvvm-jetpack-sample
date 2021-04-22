package com.common.webframe.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.common.webframe.ui.fragment.WebPageFrgment

class TabPagerAdapter(fm: FragmentManager?, private val tabFragments: List<WebPageFrgment>) : FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return tabFragments[position]
    }

    override fun getCount(): Int {
        return tabFragments.size
    }
}