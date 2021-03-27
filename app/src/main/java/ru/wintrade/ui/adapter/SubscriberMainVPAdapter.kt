package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.mvp.presenter.adapter.ISubscriberMainVPListPresenter

class SubscriberMainVPAdapter(fragment: Fragment, val presenter: ISubscriberMainVPListPresenter) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = presenter.getCount()

    override fun createFragment(position: Int): Fragment = presenter.getFragmentList()[position]
}