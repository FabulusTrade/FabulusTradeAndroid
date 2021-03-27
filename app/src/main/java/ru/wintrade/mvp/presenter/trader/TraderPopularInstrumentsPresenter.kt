package ru.wintrade.mvp.presenter.trader

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.trader.TraderPopularInstrumentsView
import javax.inject.Inject

class TraderPopularInstrumentsPresenter : MvpPresenter<TraderPopularInstrumentsView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}