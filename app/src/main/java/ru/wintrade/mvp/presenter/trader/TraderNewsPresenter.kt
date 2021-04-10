package ru.wintrade.mvp.presenter.trader

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.trader.TraderNewsView
import javax.inject.Inject

class TraderNewsPresenter : MvpPresenter<TraderNewsView>() {
    @Inject
    lateinit var router: Router

    private var state = State.PUBLICATIONS

    enum class State {
        PUBLICATIONS, SUBSCRIPTION
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun publicationsBtnClicked() {
        state = State.PUBLICATIONS
        viewState.setBtnsState(state)
    }

    fun subscriptionBtnClicked() {
        state = State.SUBSCRIPTION
        viewState.setBtnsState(state)
    }
}