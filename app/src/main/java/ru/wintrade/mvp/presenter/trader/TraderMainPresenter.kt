package ru.wintrade.mvp.presenter.trader

import android.graphics.Color
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.view.trader.TraderMainView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.formatDigitWithDef
import java.net.ProtocolException
import javax.inject.Inject

class TraderMainPresenter(val trader: Trader) : MvpPresenter<TraderMainView>() {
    companion object {
        private const val OBSERVER = 1
        private const val TRADER = 2
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var resourceProvider: ResourceProvider

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
        }
    }

    private fun setVisibility(result: Boolean) {
        isObserveActive = result
        viewState.setSubscribeBtnActive(result)
        viewState.setObserveVisibility(result)
        viewState.setObserveActive(result)
    }
}