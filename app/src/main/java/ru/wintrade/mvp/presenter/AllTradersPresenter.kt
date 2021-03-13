package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.wintrade.mvp.view.AllTradersView

class AllTradersPresenter: MvpPresenter<AllTradersView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}