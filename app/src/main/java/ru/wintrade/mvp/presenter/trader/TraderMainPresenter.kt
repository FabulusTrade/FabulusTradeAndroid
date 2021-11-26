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
import java.net.ProtocolException
import javax.inject.Inject

class TraderMainPresenter(val trader: Trader) : MvpPresenter<TraderMainView>() {
    companion object {
        private const val PROFIT_FORMAT = "0.00"
        private const val ZERO_PERCENT = "0.00%"

        private const val OBSERVER = 1
        private const val TRADER = 2
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
        apiRepo
            .getTraderStatistic(trader.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ traderStatistic ->
                val isPositiveProfit =
                    traderStatistic.incrDecrDepo365.toString().substring(0, 1) != "-"
                traderStatistic.incrDecrDepo365?.let {
                    viewState.setProfit(
                        it.doubleToStringWithFormat(PROFIT_FORMAT, true),
                        isPositiveProfit
                    )
                } ?: viewState.setProfit(ZERO_PERCENT, true)
                viewState.initVP(traderStatistic, trader)
            }, { error ->
                error.message
            })
    }

    fun observeBtnClicked() {
        isObserveActive = !isObserveActive
        viewState.setObserveActive(isObserveActive)
        if (profile.user == null) {
            router.navigateTo(Screens.signInScreen(false))
        } else if (isObserveActive) {
            apiRepo
                .observeToTrader(profile.token!!, trader.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        } else {
            apiRepo
                .deleteObservation(profile.token!!, trader.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {})
        }
    }

    fun subscribeToTraderBtnClicked() {
        when {
            profile.user == null -> {
                router.navigateTo(Screens.signInScreen(false))
            }
            isObserveActive -> {
                apiRepo
                    .subscribeToTrader(profile.token!!, trader.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        isObserveActive = !isObserveActive
                        setVisibility(false)
                    }, { error ->
                        error.printStackTrace()
                    })
            }
            !isObserveActive -> {
                apiRepo
                    .deleteObservation(profile.token!!, trader.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        // Не обрабатывется
                    }, { error ->
                        if (error is ProtocolException) {
                            isObserveActive = !isObserveActive
                            setVisibility(true)
                        } else {
                            error.printStackTrace()
                        }
                    })
            }
        }
    }

    private fun checkSubscription() {
        if (profile.user != null) {
            apiRepo
                .mySubscriptions(profile.token!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ subscriptions ->
                    subscriptions
                        .find { it.trader.id == trader.id }
                        ?.let { sub ->
                            when {
                                sub.status?.toInt() == OBSERVER -> {
                                    isObserveActive = true
                                    setVisibility(isObserveActive)
                                }
                                sub.status?.toInt() == TRADER -> {
                                    isObserveActive = false
                                    setVisibility(isObserveActive)
                                    viewState.setObserveActive(isObserveActive)
                                }
                                else -> setVisibility(false)
                            }
                        } ?: setVisibility(true)
                }, { error ->
                    error.printStackTrace()
                })
        }
    }

    private fun setVisibility(result: Boolean) {
        viewState.setSubscribeBtnActive(result)
        viewState.setObserveVisibility(result)
    }
}