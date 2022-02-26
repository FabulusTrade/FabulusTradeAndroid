package ru.fabulus.fabulustrade.mvp.presenter.trader

import android.graphics.Color
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.mvp.presenter.base.BaseTraderMvpPresenter
import ru.fabulus.fabulustrade.mvp.view.trader.TraderMainView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import java.net.ProtocolException

class TraderMainPresenter(val trader: Trader) : BaseTraderMvpPresenter<TraderMainView>() {
    companion object {
        private const val OBSERVER = 1
        private const val TRADER = 2
        private const val LOG = "VVV"
    }

    private var isObserveActive: Boolean = false

    override fun onFirstViewAttach() {
        Log.d(LOG, "TraderMainPresenter. onFirstViewAttach()")
        super.onFirstViewAttach()
        loadTraderStatistic()
        setVisibility(false)
        viewState.init()
        viewState.setUsername(trader.username!!)
        trader.avatar?.let { viewState.setAvatar(it) }
        checkSubscription()
    }

    private fun loadTraderStatistic() {
        Log.d(LOG, "TraderMainPresenter. loadTraderStatistic()")
        apiRepo
            .getTraderStatistic(trader.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ traderStatistic: TraderStatistic ->

                var tmpColor = resourceProvider.getColor(R.color.colorDarkGray)
                var tmpProfit = resourceProvider.getStringResource(R.string.empty_profit_result)

                traderStatistic.colorIncrDecrDepo365?.let { colorItem ->
                    tmpProfit = resourceProvider.formatDigitWithDef(
                        R.string.incr_decr_depo_365,
                        colorItem.value
                    )

                    colorItem.color?.let { color ->
                        tmpColor = Color.parseColor(color)
                    }
                }

                viewState.setProfit(tmpProfit, tmpColor)
                viewState.initVP(traderStatistic, trader)
            }, { error ->
                error.message
            })
    }

    fun observeBtnClicked(subscribe: Boolean) {
        Log.d(LOG, "TraderMainPresenter. observeBtnClicked()")
        viewState.setObserveChecked(subscribe)
        when {
            profile.user == null -> {
                router.navigateTo(Screens.signInScreen(false))
            }
            subscribe -> {
                apiRepo
                    .observeToTrader(profile.token!!, trader.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState.showToast(
                            resourceProvider.getStringResource(R.string.added_to_observation)
                        )
                    }, {})
            }
            else -> {
                apiRepo
                    .deleteObservation(profile.token!!, trader.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, {})
                // TODO Уточнить момент с кодом ответа 204 и перенести сообщение в subscribe success
                viewState.showToast(
                    resourceProvider.getStringResource(R.string.removed_from_observation)
                )
            }
        }
    }

    fun subscribeToTraderBtnClicked() {
        Log.d(LOG, "TraderMainPresenter. subscribeToTraderBtnClicked()")
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
                        isObserveActive = !isObserveActive
                        setVisibility(true)
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
        Log.d(LOG, "TraderMainPresenter. checkSubscription()")
        if (profile.user != null) {
            setVisibility(false)
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
                                }
                                else -> setVisibility(true)
                            }
                            if (sub.status?.toInt() == 1) {
                                viewState.setObserveChecked(true)
                            } else {
                                viewState.setObserveChecked(false)
                            }
                        } ?: setVisibility(true)
                }, { error ->
                    error.printStackTrace()
                })
        } else {
            setVisibility(true)
        }
    }

    private fun setVisibility(result: Boolean) {
        Log.d(LOG, "TraderMainPresenter. setVisibility()")
        isObserveActive = result
        viewState.setSubscribeBtnActive(result)
        viewState.setObserveVisibility(result)
        viewState.setObserveActive(result)
    }
}