package ru.wintrade.mvp.presenter.trader

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.trader.TraderMainView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class TraderMainPresenter(val trader: Trader) : MvpPresenter<TraderMainView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    var isObserveActive: Boolean = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setVisibility(true)
        viewState.setObserveActive(false)
        viewState.init()
        viewState.setUsername(trader.username!!)
        val isPositiveProfit = trader.yearProfit?.substring(0, 1) != "-"
        viewState.setProfit(trader.yearProfit!!, isPositiveProfit)
        trader.avatar?.let { viewState.setAvatar(it) }
        checkSubscription()
    }

    fun observeBtnClicked() {
        isObserveActive = !isObserveActive
        viewState.setObserveActive(isObserveActive)
        if (profile.user == null) {
            router.navigateTo(Screens.SignInScreen())
        } else if (isObserveActive) {
            apiRepo.observeToTrader(profile.token!!, trader.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        } else {
            apiRepo.deleteObservation(profile.token!!, trader.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {})
        }
    }

    fun subscribeToTraderBtnClicked() {
        if (profile.user == null)
            router.navigateTo(Screens.SignInScreen())
        else
            apiRepo.subscribeToTrader(profile.token!!, trader.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.setSubscribeBtnActive(false)
                    viewState.setObserveVisibility(false)
                }, {
                    it.printStackTrace()
                })
    }

    private fun checkSubscription() {
        if (profile.user != null) {
            apiRepo.mySubscriptions(profile.token!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ subscriptions ->
                    subscriptions.find { it.trader.id == trader.id }?.let {
                        if (it.status?.toInt() == 1) {
                            setVisibility(true)
                            isObserveActive = true
                            viewState.setObserveActive(isObserveActive)
                        } else
                            setVisibility(false)
                    } ?: setVisibility(true)
                }, {
                    it.printStackTrace()
                })
        }
    }

    private fun setVisibility(result: Boolean) {
        viewState.setSubscribeBtnActive(result)
        viewState.setObserveVisibility(result)
    }
}