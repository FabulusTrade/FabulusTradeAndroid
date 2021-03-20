package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.mvp.presenter.adapter.ITraderStatVPListPresenter

class TraderVPAdapter(fragment: Fragment, val presenter: ITraderStatVPListPresenter) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = presenter.getCount()

    override fun createFragment(position: Int): Fragment = presenter.getFragmentList()[position]
}