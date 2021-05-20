package ru.wintrade.mvp.presenter.traderme

import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.view.traderme.TraderMeMainView
import javax.inject.Inject

class TraderMeMainPresenter: MvpPresenter<TraderMeMainView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setProfit("96%", true)
        viewState.setSubscriberCount(profile.user!!.subscriptionsCount)
        viewState.setUsername(profile.user!!.username)
        profile.user!!.avatar?.let {viewState.setAvatar(it)}
    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }
}