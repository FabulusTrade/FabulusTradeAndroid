package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.ui.fragment.traders.TradersAllFragment
import ru.wintrade.ui.fragment.traders.TradersFilterFragment

class TradersMainVPAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    private val fragmentList = listOf<Fragment>(
        TradersAllFragment.newInstance(),
        TradersFilterFragment.newInstance()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}