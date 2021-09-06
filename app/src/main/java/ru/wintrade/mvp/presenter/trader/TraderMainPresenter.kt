package ru.wintrade.mvp.presenter.trader

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.trader.TraderMainView
import ru.wintrade.navigation.Screens
import java.text.DecimalFormat
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
        loadTraderStatistic()
        setVisibility(true)
        viewState.setObserveActive(false)
        viewState.init()
        viewState.setUsername(trader.username!!)
        trader.avatar?.let { viewState.setAvatar(it) }
        checkSubscription()
    }

    private fun loadTraderStatistic() {
        profile.token?.let { token ->
            apiRepo.getTraderStatistic(token, trader.id)
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ traderStatistic ->
                    val isPositiveProfit =
                        traderStatistic.actualProfit365.toString().substring(0, 1) != "-"
                    traderStatistic.actualProfit365?.let {
                        viewState.setProfit(
                            "${DecimalFormat("#0.0").format(it)}%",
                            isPositiveProfit
                        )
                    } ?: viewState.setProfit("0.0%", true)
                    viewState.initVP(traderStatistic, trader)
                }, {
                })
        }
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