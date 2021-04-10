package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.ui.fragment.subscriber.SubscriberTradeFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberNewsFragment
import ru.wintrade.ui.fragment.subscriber.SubscriberObservationFragment

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