package ru.fabulus.fabulustrade.mvp.presenter.subscriber

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.fabulus.fabulustrade.mvp.presenter.base.BaseTraderMvpPresenter
import ru.fabulus.fabulustrade.mvp.view.subscriber.SubscriberMainView

class SubscriberMainPresenter : BaseTraderMvpPresenter<SubscriberMainView>() {

    companion object {
        private const val LOG = "VVV"
    }

    override fun onFirstViewAttach() {
        Log.d(LOG, "SubscriberMainPresenter. onFirstViewAttach()")
        super.onFirstViewAttach()
        viewState.init()
        viewState.setAvatar(profile.user!!.avatar)
        viewState.setName(profile.user!!.username)
        getSubscriptionCount()
    }

    private fun getSubscriptionCount() {
        Log.d(LOG, "SubscriberMainPresenter. getSubscriptionCount()")
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