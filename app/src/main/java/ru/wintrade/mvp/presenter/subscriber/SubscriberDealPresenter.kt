package ru.wintrade.mvp.presenter.subscriber

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.subscriber.SubscriberDealView
import javax.inject.Inject

class SubscriberDealPresenter : MvpPresenter<SubscriberDealView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}