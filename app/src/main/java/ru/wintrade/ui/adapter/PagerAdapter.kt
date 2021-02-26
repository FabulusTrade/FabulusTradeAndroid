package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragment: Fragment, private val fragmentsList: ArrayList<Fragment>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragmentsList.size

    override fun createFragment(position: Int): Fragment = fragmentsList[position]
}