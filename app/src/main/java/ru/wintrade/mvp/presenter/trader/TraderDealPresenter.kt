package ru.wintrade.mvp.presenter.trader

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.trader.TraderDealView
import javax.inject.Inject

class TraderDealPresenter : MvpPresenter<TraderDealView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}