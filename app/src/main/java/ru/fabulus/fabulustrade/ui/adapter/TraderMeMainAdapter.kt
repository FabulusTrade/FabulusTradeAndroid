package ru.fabulus.fabulustrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeObservationFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMePostFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeProfitFragment
import ru.fabulus.fabulustrade.ui.fragment.traderme.TraderMeTradeFragment

class TraderMeMainAdapter(fragment: Fragment, traderStatistic: TraderStatistic) :
    FragmentStateAdapter(fragment) {

    private val fragmentList: MutableList<Fragment> = mutableListOf(
        TraderMeProfitFragment.newInstance(traderStatistic),
        TraderMePostFragment.newInstance(),
        // TODO скрываем меню, до момента пока не реализуем обработку событий
//        TraderPopularInstrumentsFragment.newInstance(),
        TraderMeTradeFragment.newInstance(),
        TraderMeObservationFragment.newInstance()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun getFragment(position: Int): Fragment = fragmentList[position]
}