package ru.wintrade.mvp.presenter.subscriber

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.subscriber.SubscriberMainView
import javax.inject.Inject

class SubscriberMainPresenter : MvpPresenter<SubscriberMainView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}