package ru.fabulus.fabulustrade.mvp.presenter.subscriber

import ru.fabulus.fabulustrade.mvp.presenter.base.BaseTraderMvpPresenter
import ru.fabulus.fabulustrade.mvp.view.subscriber.SubscriberMainView

class SubscriberMainPresenter : BaseTraderMvpPresenter<SubscriberMainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setAvatar(profile.user!!.avatar)
        viewState.setName(profile.user!!.username)
    }
}