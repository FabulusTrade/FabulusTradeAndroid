package ru.wintrade.mvp.presenter.trader

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.trader.TraderObservationView
import javax.inject.Inject

class TraderObservationPresenter : MvpPresenter<TraderObservationView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}