package ru.wintrade.mvp.presenter.trader

import androidx.fragment.app.Fragment
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.presenter.adapter.ITraderStatVPListPresenter
import ru.wintrade.mvp.view.trader.TraderStatView
import ru.wintrade.navigation.Screens
import ru.wintrade.ui.fragment.trader.TraderDealFragment
import ru.wintrade.ui.fragment.trader.TraderNewsFragment
import ru.wintrade.ui.fragment.trader.TraderPopularInstrumentsFragment
import ru.wintrade.ui.fragment.trader.TraderProfitFragment
import javax.inject.Inject

class TraderStatPresenter(val trader: Trader) : MvpPresenter<TraderStatView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    val listPresenter = TraderVPListPresenter()

    inner class TraderVPListPresenter : ITraderStatVPListPresenter {
        private val viewPagerListOfFragment = mutableListOf<Fragment>(
            TraderProfitFragment.newInstance(trader),
            TraderNewsFragment.newInstance(),
            TraderPopularInstrumentsFragment.newInstance(),
            TraderDealFragment.newInstance()
        )

        override fun getCount(): Int = viewPagerListOfFragment.size

        override fun getFragmentList(): List<Fragment> {
            return viewPagerListOfFragment
        }
    }

    fun backClicked(): Boolean {
        router.backTo(Screens.TradersMainScreen())
        return true
    }
}