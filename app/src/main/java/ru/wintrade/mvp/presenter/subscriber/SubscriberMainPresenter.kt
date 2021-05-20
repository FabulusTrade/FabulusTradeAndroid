package ru.wintrade.mvp.presenter.subscriber

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.subscriber.SubscriberMainView
import javax.inject.Inject

class SubscriberMainPresenter : MvpPresenter<SubscriberMainView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setAvatar(profile.user!!.avatar)
        viewState.setName(profile.user!!.username)
        getSubscriptionCount()
    }

    fun getSubscriptionCount() {
        apiRepo.getProfile(profile.token!!).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setSubscriptionCount(it.subscriptions_count)
            }, {
                it.printStackTrace()
            })
    }
}