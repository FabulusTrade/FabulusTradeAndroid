package ru.wintrade.mvp.presenter.subscriber

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.presenter.adapter.ISubscriberObservationListPresenter
import ru.wintrade.mvp.view.item.SubscriberObservationItemView
import ru.wintrade.mvp.view.subscriber.SubscriberObservationView
import javax.inject.Inject

class SubscriberObservationPresenter : MvpPresenter<SubscriberObservationView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    var listPresenter = SubscriberObservationListPresenter()

    inner class SubscriberObservationListPresenter: ISubscriberObservationListPresenter {
        var subscriberObservationList = mutableListOf<Trader>()
        override fun getCount(): Int = subscriberObservationList.size

        override fun bind(view: SubscriberObservationItemView) {
            
        }

    }
}