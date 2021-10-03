package ru.wintrade.mvp.presenter.trader

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.trader.TraderMainView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.doubleToStringWithFormat
import javax.inject.Inject

class TraderMainPresenter(val trader: Trader) : MvpPresenter<TraderMainView>() {
    companion object {
        private const val PROFIT_FORMAT = "0.00"
        private const val ZERO_PERCENT = "0.00%"
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    var isObserveActive: Boolean = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadTraderStatistic()
        setVisibility(true)
        viewState.setObserveActive(false)
        viewState.init()
        viewState.setUsername(trader.username!!)
        trader.avatar?.let { viewState.setAvatar(it) }
        checkSubscription()
    }

    private fun loadTraderStatistic() {
        apiRepo.getTraderStatistic(trader.id)
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ traderStatistic ->
                val isPositiveProfit =
                    traderStatistic.actualProfit365.toString().substring(0, 1) != "-"
                traderStatistic.actualProfit365?.let {
                    viewState.setProfit(
                        it.doubleToStringWithFormat(PROFIT_FORMAT, true),
                        isPositiveProfit
                    )
                } ?: viewState.setProfit(ZERO_PERCENT, true)
                viewState.initVP(traderStatistic, trader)
            }, {
                it.message
            })
    }

    fun observeBtnClicked() {
        isObserveActive = !isObserveActive
        viewState.setObserveActive(isObserveActive)
        if (profile.user == null) {
            router.navigateTo(Screens.signInScreen())
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
            router.navigateTo(Screens.signInScreen())
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