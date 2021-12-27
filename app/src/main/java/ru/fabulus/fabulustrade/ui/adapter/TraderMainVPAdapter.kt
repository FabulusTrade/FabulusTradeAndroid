package ru.fabulus.fabulustrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderPostFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderProfitFragment
import ru.fabulus.fabulustrade.ui.fragment.trader.TraderTradeFragment

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