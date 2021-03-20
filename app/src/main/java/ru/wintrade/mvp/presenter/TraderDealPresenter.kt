package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.TraderDealView
import javax.inject.Inject

class TraderDealPresenter : MvpPresenter<TraderDealView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}