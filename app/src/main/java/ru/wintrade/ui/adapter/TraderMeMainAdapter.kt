package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.wintrade.ui.fragment.trader.TraderTradeFragment
import ru.wintrade.ui.fragment.traderme.*

class TraderMeMainAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private var fragmentList: MutableList<Fragment> = mutableListOf(
        TraderMeProfitFragment.newInstance(),
        TraderMePostFragment.newInstance(),
        TraderPopularInstrumentsFragment.newInstance(),
        TraderTradeFragment.newInstance(),
        TraderMeObservationFragment.newInstance()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}