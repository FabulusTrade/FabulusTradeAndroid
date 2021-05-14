package ru.wintrade.mvp.presenter.traderforsubscriber

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.traderforsubscriber.TraderForSubscriberMainView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class TraderForSubscriberMainPresenter(val trader: Trader, val isAuthorized: Boolean) :
    MvpPresenter<TraderForSubscriberMainView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profileStorage: ProfileStorage

    var isObserveActive: Boolean = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        checkSubscription(isAuthorized)
    }

    fun observeBtnClicked(isAuthorized: Boolean) {
        if (!isAuthorized) router.navigateTo(Screens.SignInScreen())
        else {
            isObserveActive = !isObserveActive
            viewState.setObserveActive(isObserveActive)
        }
    }

    fun subscribeToTraderBtnClicked(isAuthorized: Boolean) {
        if (!isAuthorized) router.navigateTo(Screens.SignInScreen())
        else {
            profileStorage.profile?.let { profile ->
                apiRepo.subscribeToTrader(profile.token, trader.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState.setSubscribeBtnActive(false)
                        viewState.setObserveVisibility(false)
                    }, {
                        it.printStackTrace()
                    })
            }
        }
    }

    private fun checkSubscription(isAuthorized: Boolean) {
        if (!isAuthorized) {
            setVisibility(true)
        } else {
            profileStorage.profile?.let { profile ->
                apiRepo.mySubscriptions(profile.token)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ subscriptions ->
                        subscriptions.find { it.trader.id == trader.id }?.let {
                            setVisibility(false)
                        } ?: setVisibility(true)
                    }, {
                        it.printStackTrace()
                    })
            }
        }
    }

    private fun setVisibility(result: Boolean) {
        viewState.setSubscribeBtnActive(result)
        viewState.setObserveVisibility(result)
    }

    fun backClicked(): Boolean {
        router.backTo(Screens.TradersMainScreen())
        return true
    }
}