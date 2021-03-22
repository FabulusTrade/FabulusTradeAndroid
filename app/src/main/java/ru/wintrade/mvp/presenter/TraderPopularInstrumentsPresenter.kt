package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.TraderPopularInstrumentsView
import javax.inject.Inject

class TraderPopularInstrumentsPresenter : MvpPresenter<TraderPopularInstrumentsView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}