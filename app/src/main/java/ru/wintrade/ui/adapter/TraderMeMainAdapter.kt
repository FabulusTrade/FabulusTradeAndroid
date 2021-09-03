package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.wintrade.ui.fragment.traderme.TraderMeObservationFragment
import ru.wintrade.ui.fragment.traderme.TraderMePostFragment
import ru.wintrade.ui.fragment.traderme.TraderMeProfitFragment
import ru.wintrade.ui.fragment.traderme.TraderMeTradeFragment

class TraderMeMainAdapter(fragment: Fragment, traderStatistic: TraderStatistic) :
    FragmentStateAdapter(fragment) {

    private val fragmentList: MutableList<Fragment> = mutableListOf(
        TraderMeProfitFragment.newInstance(traderStatistic),
        TraderMePostFragment.newInstance(),
        TraderPopularInstrumentsFragment.newInstance(),
        TraderMeTradeFragment.newInstance(),
        TraderMeObservationFragment.newInstance()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}