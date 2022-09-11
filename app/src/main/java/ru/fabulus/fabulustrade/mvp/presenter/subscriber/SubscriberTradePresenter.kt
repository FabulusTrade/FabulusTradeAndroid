package ru.fabulus.fabulustrade.mvp.presenter.subscriber

import android.annotation.SuppressLint
import android.view.View
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.Trade
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.ISubscriberTradesRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.SubscriberTradeItemView
import ru.fabulus.fabulustrade.mvp.view.subscriber.SubscriberDealView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.formatString
import ru.fabulus.fabulustrade.util.toStringFormat
import javax.inject.Inject

class SubscriberTradePresenter : MvpPresenter<SubscriberDealView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var resourceProvider: ResourceProvider

    enum class State {
        DEALS, ORDERS, JOURNAL
    }

    private var state = State.DEALS
    private var isLoading = false
    private var nextPage: Int? = 1

    val listPresenter = SubscriberTradesRVListPresenter()
    val traders = mutableListOf<Trader>()
    var newTradeDisposable: Disposable? = null

    inner class SubscriberTradesRVListPresenter : ISubscriberTradesRVListPresenter {
        val trades = mutableListOf<Trade>()

        override fun getCount() = trades.size

        @SuppressLint("SimpleDateFormat")
        override fun bind(view: SubscriberTradeItemView) {
            val trade = trades[view.pos]
            trade.trader?.let { trader ->
                trader.avatar?.let { avatar -> view.setAvatar(avatar) }
                trader.username?.let { username -> view.setNickname(username) }
            }
            view.setCompany(
                resourceProvider.formatString(
                    R.string.deal_company_name,
                    trade.company,
                    trade.ticker
                )
            )
            view.setDate(
                resourceProvider.formatString(
                    R.string.deal_date,
                    trade.date.toStringFormat()
                )
            )
            view.setType(trade.operationType)
            view.setPrice(
                resourceProvider.formatString(
                    R.string.deal_price,
                    trade.price,
                    trade.currency
                )
            )
            trade.profitCount?.let { profitCount ->
                val profitCountStr =
                    resourceProvider.formatString(R.string.deal_profit_count, profitCount)
                if (profitCount.toFloat() >= 0) {
                    view.setProfit(profitCountStr, R.color.colorGreen)
                } else {
                    view.setProfit(profitCountStr, R.color.colorRed)
                }
                view.setProfitVisibility(View.VISIBLE)
            } ?: view.setProfitVisibility(View.GONE)
        }

        override fun clicked(pos: Int) {
            val trade = trades[pos]
            router.navigateTo(Screens.tradeDetailScreen(trade, false))
        }
    }

    fun refreshed() {
        refreshTrades()
    }

    fun onScrollLimit() {
        loadNextPage()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        apiRepo.mySubscriptions(profile.token!!).observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { subscriptions ->
                    traders.addAll(subscriptions.map { it.trader })
                    refreshTrades()
                    apiRepo.newTradeSubject.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getNewTradeObserver())
                },
                {}
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        newTradeDisposable?.dispose()
    }

    private fun refreshTrades() {
        viewState.setRefreshing(true)
        nextPage = 1
        isLoading = true
        apiRepo.subscriptionTrades(profile.token!!, nextPage!!)
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                { pagination ->
                    if (pagination.results.isEmpty()) {
                        viewState.withoutSubscribeAnyTrader()
                        return@subscribe
                    }
                    listPresenter.trades.clear()
                    listPresenter.trades.addAll(pagination.results)
                    resolveTraderInTrade()
                    viewState.updateAdapter()
                    nextPage = pagination.next
                    isLoading = false
                    viewState.setRefreshing(false)
                },
                {
                    it.printStackTrace()
                    isLoading = false
                    viewState.setRefreshing(false)
                }
            )
    }

    private fun loadNextPage() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            apiRepo.subscriptionTrades(profile.token!!, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { pagination ->
                        if (pagination.results.isEmpty()) {
                            viewState.withoutSubscribeAnyTrader()
                            return@subscribe
                        }
                        listPresenter.trades.addAll(pagination.results)
                        resolveTraderInTrade()
                        viewState.updateAdapter()
                        nextPage = pagination.next
                        isLoading = false
                    },
                    {
                        it.printStackTrace()
                        isLoading = false
                    }
                )
        }
    }

    fun dealsBtnClicked() {
        state = State.DEALS
        viewState.setBtnState(state)
    }

    fun ordersBtnClicked() {
        state = State.ORDERS
        viewState.setBtnState(state)
    }

    fun journalBtnClicked() {
        state = State.JOURNAL
        viewState.setBtnState(state)
    }

    private fun getNewTradeObserver() = object : Observer<Boolean> {
        override fun onSubscribe(d: Disposable) {
            newTradeDisposable = d
        }

        override fun onNext(t: Boolean) {
            refreshTrades()
        }

        override fun onError(e: Throwable) {

        }

        override fun onComplete() {

        }
    }

    private fun resolveTraderInTrade() {
        listPresenter.trades.forEach { trade ->
            if (trade.trader == null)
                trade.trader = traders.find { it.id == trade.traderId }
        }
    }
}