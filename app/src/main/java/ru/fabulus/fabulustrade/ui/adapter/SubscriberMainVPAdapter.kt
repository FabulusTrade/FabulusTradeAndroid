package ru.fabulus.fabulustrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberNewsFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberObservationFragment
import ru.fabulus.fabulustrade.ui.fragment.subscriber.SubscriberTradeFragment

class SubscriberMainVPAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val fragmentList = listOf<Fragment>(
        SubscriberObservationFragment.newInstance(),
        SubscriberTradeFragment.newInstance(),
        SubscriberNewsFragment.newInstance()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}