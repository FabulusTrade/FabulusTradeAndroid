package ru.fabulus.fabulustrade.mvp.presenter.subscriber

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.fabulus.fabulustrade.mvp.presenter.base.BaseTraderMvpPresenter
import ru.fabulus.fabulustrade.mvp.view.subscriber.SubscriberMainView

class SubscriberMainPresenter : BaseTraderMvpPresenter<SubscriberMainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setAvatar(profile.user!!.avatar)
        viewState.setName(profile.user!!.username)
        getSubscriptionCount()
    }

    private fun getSubscriptionCount() {
        apiRepo
            .getProfile(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setSubscriptionCount(it.subscriptions_count)
            }, {
                it.printStackTrace()
            })
    }
}