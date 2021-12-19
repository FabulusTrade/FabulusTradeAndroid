package ru.wintrade.mvp.presenter.traders

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Subscription
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.presenter.adapter.ITradersAllListPresenter
import ru.wintrade.mvp.view.item.TradersAllItemView
import ru.wintrade.mvp.view.traders.TradersAllView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.formatDigitWithDef
import ru.wintrade.util.stringColorToIntWithDef
import javax.inject.Inject

class TradersAllPresenter(val checkedFilter: Int) : MvpPresenter<TradersAllView>() {
    companion object {
        const val DEFAULT_FILTER = 0
        const val BY_FOLLOWERS = 1
    }

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var resourceProvider: ResourceProvider

    val listPresenter = TradersAllListPresenter()
    var subscriptionList = mutableListOf<Subscription>()
    private var isLoading = false
    private var nextPage: Int? = 1

    inner class TradersAllListPresenter : ITradersAllListPresenter {
        var traderList = mutableListOf<Trader>()
        override fun getCount(): Int = traderList.size

        override fun bind(view: TradersAllItemView) {
            val trader = traderList[view.pos]
            trader.username?.let { userName -> view.setTraderName(userName) }

            view.setTraderProfit(
                resourceProvider.formatDigitWithDef(
                    R.string.tv_traders_all_item_year_profit_text,
                    trader.colorIncrDecrDepo365?.value
                ),
                resourceProvider.stringColorToIntWithDef(trader.colorIncrDecrDepo365?.color)
            )
            trader.avatar?.let { avatar -> view.setTraderAvatar(avatar) }
            view.setTraderObserveBtn(false)
            subscriptionList.forEach { subscription ->
                if (subscription.trader.id == trader.id && subscription.status?.toInt() == 2)
                    view.setTraderObserveBtn(null)
                else if (subscription.trader.id == trader.id && subscription.status?.toInt() != 2)
                    view.setTraderObserveBtn(true)
            }
        }

        override fun openTraderStat(pos: Int) {
            val trader = traderList[pos]
            if (isLogged() && trader.id == profile.user!!.id)
                router.navigateTo(Screens.traderMeMainScreen())
            else
                router.navigateTo(Screens.traderMainScreen(traderList[pos]))
        }

        override fun checkIfTraderIsMe(pos: Int): Boolean {
            return profile.user?.id.equals(traderList[pos].id)
        }

        override fun isLogged(): Boolean {
            return profile.user != null
        }

        override fun observeBtnClicked(pos: Int, isChecked: Boolean) {
            if (profile.user == null) {
                router.navigateTo(Screens.signInScreen(false))
                return
            }

            if (checkIfTraderIsMe(pos)) {
                return
            }

            if (isChecked) {
                apiRepo
                    .observeToTrader(profile.token!!, traderList[pos].id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            } else {
                apiRepo
                    .deleteObservation(profile.token!!, traderList[pos].id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, {})
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        when (checkedFilter) {
            DEFAULT_FILTER -> loadTraders()
            BY_FOLLOWERS -> loadTradersFilteredByFollowers()
        }
    }

    override fun attachView(view: TradersAllView?) {
        super.attachView(view)
        loadMySubscription()
    }

    fun onScrollLimit() {
        when (checkedFilter) {
            DEFAULT_FILTER -> loadTraders()
            BY_FOLLOWERS -> loadTradersFilteredByFollowers()
        }
        loadMySubscription()
    }

    private fun loadMySubscription() {
        profile.token?.let {
            apiRepo
                .mySubscriptions(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    subscriptionList.clear()
                    subscriptionList.addAll(it)
                    viewState.updateAdapter()
                }, {
                    // Ошибка не обрабатывается
                })
        }
    }

    private fun loadTraders() {
        viewState.setFilterText(R.string.cb_filter_profit_label)
        if (nextPage != null && !isLoading) {
            isLoading = true
            apiRepo
                .getTradersProfitFiltered(nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    listPresenter.traderList.addAll(pag.results)
                    viewState.updateAdapter()
                    nextPage = pag.next
                    isLoading = false
                }, {
                    it.printStackTrace()
                    isLoading = false
                })
        }
    }

    private fun loadTradersFilteredByFollowers() {
        viewState.setFilterText(R.string.cb_filter_followers_label)
        if (nextPage != null && !isLoading) {
            isLoading = true

            apiRepo
                .getTradersFollowersFiltered(nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    listPresenter.traderList.addAll(pag.results)
                    viewState.updateAdapter()
                    nextPage = pag.next
                    isLoading = false
                }, {
                    it.printStackTrace()
                    isLoading = false
                })
        }
    }
}