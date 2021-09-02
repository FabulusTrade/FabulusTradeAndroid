package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.wintrade.ui.fragment.trader.TraderPostFragment
import ru.wintrade.ui.fragment.trader.TraderProfitFragment
import ru.wintrade.ui.fragment.trader.TraderTradeFragment

class TraderMainVPAdapter(fragment: Fragment, traderStatistic: TraderStatistic, trader: Trader) :
    FragmentStateAdapter(fragment) {

    private val fragmentList: MutableList<Fragment> = mutableListOf(
        TraderProfitFragment.newInstance(traderStatistic, trader),
        TraderPostFragment.newInstance(trader),
        TraderPopularInstrumentsFragment.newInstance(),
        TraderTradeFragment.newInstance(trader)
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}