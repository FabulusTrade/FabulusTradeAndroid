package ru.wintrade.mvp.presenter

import moxy.MvpPresenter
import ru.wintrade.mvp.view.HomeView

class HomePresenter: MvpPresenter<HomeView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}