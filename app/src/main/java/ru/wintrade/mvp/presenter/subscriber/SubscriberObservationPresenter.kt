package ru.wintrade.mvp.presenter.subscriber

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.view.subscriber.SubscriberObservationView
import javax.inject.Inject

class SubscriberObservationPresenter : MvpPresenter<SubscriberObservationView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}