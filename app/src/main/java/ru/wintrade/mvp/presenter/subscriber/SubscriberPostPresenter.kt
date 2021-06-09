package ru.wintrade.mvp.presenter.subscriber

import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import ru.wintrade.mvp.view.subscriber.SubscriberNewsView
import javax.inject.Inject

class SubscriberPostPresenter : MvpPresenter<SubscriberNewsView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }
}