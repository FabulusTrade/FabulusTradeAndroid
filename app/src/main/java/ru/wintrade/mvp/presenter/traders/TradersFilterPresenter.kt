package ru.wintrade.mvp.presenter.traders

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.traders.TradersFilterView
import javax.inject.Inject

class TradersFilterPresenter : MvpPresenter<TradersFilterView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}