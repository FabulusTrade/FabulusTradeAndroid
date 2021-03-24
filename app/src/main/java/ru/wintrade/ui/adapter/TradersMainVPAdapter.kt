package ru.wintrade.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.wintrade.mvp.presenter.adapter.ITradersMainVPListPresenter

class TradersMainVPAdapter(fragment: Fragment, val presenterMain: ITradersMainVPListPresenter) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = presenterMain.getCount()

    override fun createFragment(position: Int): Fragment = presenterMain.getFragmentList()[position]
}