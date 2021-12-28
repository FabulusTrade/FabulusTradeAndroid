package ru.fabulus.fabulustrade.mvp.presenter

import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.view.AboutWinTradeView

class AboutWinTradePresenter : MvpPresenter<AboutWinTradeView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}