package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.wintrade.ui.fragment.traderme.*

class TraderMeMainAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private val SIZE = 5

    override fun getItemCount(): Int = SIZE

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> TraderMeProfitFragment.newInstance()
            1 -> TraderMePostFragment.newInstance()
            2 -> TraderPopularInstrumentsFragment.newInstance()
            3 -> TraderMeTradeFragment.newInstance()
            4 -> TraderMeObservationFragment.newInstance()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}