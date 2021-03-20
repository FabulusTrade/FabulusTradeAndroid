package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.TraderNewsView
import javax.inject.Inject

class TraderNewsPresenter : MvpPresenter<TraderNewsView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}