package ru.wintrade.mvp.presenter.traderme

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.traderme.TraderMeObservationView
import javax.inject.Inject

class TraderMeObservationPresenter : MvpPresenter<TraderMeObservationView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}