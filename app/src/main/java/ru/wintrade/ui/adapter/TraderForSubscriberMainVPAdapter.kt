package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.ui.fragment.trader.*

class TraderForSubscriberMainVPAdapter(fragment: Fragment, trader: Trader) :
    FragmentStateAdapter(fragment) {

    private val fragmentList = mutableListOf<Fragment>(
        TraderProfitFragment.newInstance(trader),
        TraderPostFragment.newInstance(),
        TraderPopularInstrumentsFragment.newInstance(),
        TraderTradeFragment.newInstance()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}