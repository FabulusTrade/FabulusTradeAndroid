package ru.fabulus.fabulustrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.fabulus.fabulustrade.ui.fragment.traders.TradersAllFragment
import ru.fabulus.fabulustrade.ui.fragment.traders.TradersFilterFragment

class TradersMainVPAdapter(fragment: Fragment, checkedFilter: Int?) :
    FragmentStateAdapter(fragment) {
    private val fragmentList = listOf<Fragment>(
        TradersAllFragment.newInstance(checkedFilter),
        TradersFilterFragment.newInstance(checkedFilter)
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}