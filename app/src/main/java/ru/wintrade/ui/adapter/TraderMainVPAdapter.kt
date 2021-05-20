package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.wintrade.ui.fragment.trader.TraderPostFragment
import ru.wintrade.ui.fragment.trader.TraderProfitFragment
import ru.wintrade.ui.fragment.trader.TraderTradeFragment
import ru.wintrade.ui.fragment.traderme.*

class TraderMainVPAdapter(fragment: Fragment, trader: Trader) :
    FragmentStateAdapter(fragment) {

    private var fragmentList: MutableList<Fragment> = mutableListOf(
        TraderProfitFragment.newInstance(trader),
        TraderPostFragment.newInstance(trader),
        TraderPopularInstrumentsFragment.newInstance(),
        TraderTradeFragment.newInstance()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}