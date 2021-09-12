package ru.wintrade.mvp.presenter.traders

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Subscription
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITradersAllListPresenter
import ru.wintrade.mvp.view.item.TradersAllItemView
import ru.wintrade.mvp.view.traders.TradersAllView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.doubleToStringWithFormat
import javax.inject.Inject

class TradersAllPresenter : MvpPresenter<TradersAllView>() {
    companion object {
        private const val FORMAT = "0.00"
    }

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    val listPresenter = TradersAllListPresenter()
    var subscriptionList = mutableListOf<Subscription>()
    private var isLoading = false
    private var nextPage: Int? = 1

    inner class TradersAllListPresenter : ITradersAllListPresenter {
        var traderList = mutableListOf<Trader>()
        override fun getCount(): Int = traderList.size

        override fun bind(view: TradersAllItemView) {
            traderList[view.pos].username?.let { view.setTraderName(it) }
            traderList[view.pos].yearProfit?.let {
                view.setTraderProfit(
                    it.doubleToStringWithFormat(FORMAT, true)
                )
            }
            traderList[view.pos].avatar?.let { view.setTraderAvatar(it) }
            subscriptionList.forEach {
                if (it.trader.id == traderList[view.pos].id && it.status?.toInt() == 2)
                    view.setTraderObserveBtn(null)
                else if (it.trader.id == traderList[view.pos].id && it.status?.toInt() != 2)
                    view.setTraderObserveBtn(true)
            }
        }

        override fun openTraderStat(pos: Int) {
            val trader = traderList[pos]
            if (profile.user != null && trader.id == profile.user!!.id)
                router.navigateTo(Screens.TraderMeMainScreen())
            else
                router.navigateTo(Screens.TraderMainScreen(traderList[pos]))
        }

        override fun observeBtnClicked(pos: Int, isChecked: Boolean) {
            if (profile.user == null) {
                router.navigateTo(Screens.SignInScreen())
            } else if (isChecked) {
                apiRepo.observeToTrader(profile.token!!, traderList[pos].id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            } else {
                apiRepo.deleteObservation(profile.token!!, traderList[pos].id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, {})
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadMySubscription()
        loadTraders()
    }

    fun onScrollLimit() {
        loadTraders()
        loadMySubscription()
    }

    private fun loadMySubscription() {
        profile.token?.let {
            apiRepo.mySubscriptions(it).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    subscriptionList.clear()
                    subscriptionList.addAll(it)
                }, {

                })
        }
    }

    private fun loadTraders() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            apiRepo.getAllTraders(nextPage!!).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { pag ->
                        listPresenter.traderList.addAll(pag.results)
                        viewState.updateAdapter()
                        nextPage = pag.next
                        isLoading = false
                    },
                    {
                        it.printStackTrace()
                        isLoading = false
                    }
                )
        }
    }
}