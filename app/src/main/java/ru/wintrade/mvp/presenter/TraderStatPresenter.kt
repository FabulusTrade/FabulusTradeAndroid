package ru.wintrade.mvp.presenter

import androidx.fragment.app.Fragment
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.presenter.adapter.ITraderStatVPListPresenter
import ru.wintrade.mvp.view.TraderStatView
import ru.wintrade.navigation.Screens
import ru.wintrade.ui.fragment.TraderProfitFragment
import javax.inject.Inject

class TraderStatPresenter : MvpPresenter<TraderStatView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    val listPresenter = TraderVPListPresenter()

    inner class TraderVPListPresenter : ITraderStatVPListPresenter {
        private val viewPagerListOfFragment = mutableListOf<Fragment>(
            TraderProfitFragment.newInstance(),
            TraderProfitFragment.newInstance(),
            TraderProfitFragment.newInstance(),
            TraderProfitFragment.newInstance()
        )

        override fun getCount(): Int = viewPagerListOfFragment.size

        override fun getFragmentList(): List<Fragment> {
            return viewPagerListOfFragment
        }
    }

    fun backClicked(): Boolean {
        router.backTo(Screens.AllTradersScreen())
        return true
    }
}