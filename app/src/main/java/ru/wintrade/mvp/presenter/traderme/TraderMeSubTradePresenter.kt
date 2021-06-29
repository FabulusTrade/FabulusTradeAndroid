package ru.wintrade.mvp.presenter.traderme

import android.annotation.SuppressLint
import android.graphics.Color
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ISubscriberTradesRVListPresenter
import ru.wintrade.mvp.view.item.SubscriberTradeItemView
import ru.wintrade.mvp.view.traderme.TraderMeSubTradeView
import ru.wintrade.navigation.Screens
import java.text.SimpleDateFormat
import javax.inject.Inject

class TraderMeSubTradePresenter : MvpPresenter<TraderMeSubTradeView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    lateinit var state: State
    private var isLoading = false
    private var nextPage: Int? = 1

    enum class State {
        DEALS, ORDERS, JOURNAL
    }

    val listPresenter = TradesRVListPresenter()
    val traders = mutableListOf<Trader>()
    var newTradeDisposable: Disposable? = null

    inner class TradesRVListPresenter : ISubscriberTradesRVListPresenter {
        val trades = mutableListOf<Trade>()

        override fun getCount() = trades.size

        @SuppressLint("SimpleDateFormat")
        override fun bind(view: SubscriberTradeItemView) {
            val trade = trades[view.pos]
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val date = dateFormat.format(trade.date)
            trade.trader?.let { trader ->
                trader.avatar?.let { avatar -> view.setAvatar(avatar) }
                trader.username?.let { username -> view.setNickname(username) }
            }
            view.setCompany(trade.company)
            view.setCount("Кол-во: ${trade.count}")
            view.setDate("Дата: $date")
            view.setType(trade.operationType)
            view.setPrice("Цена: ${trade.price} ${trade.currency}")
            view.setSum("Сумма: ${trade.value} ${trade.currency}")
            trade.profitCount?.let {
                if (trade.profitCount.toFloat() >= 0) {
                    view.setProfit("${trade.profitCount} %", Color.GREEN)
                } else {
                    view.setProfit("${trade.profitCount} %", Color.RED)
                }
            } ?: view.setProfit("", Color.WHITE)
        }

        override fun clicked(pos: Int) {
            val trade = trades[pos]
            router.navigateTo(Screens.TradeDetailScreen(trade))
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