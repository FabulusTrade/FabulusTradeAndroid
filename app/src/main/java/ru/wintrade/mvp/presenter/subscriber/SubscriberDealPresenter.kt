package ru.wintrade.mvp.presenter.subscriber

import android.graphics.Color
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.common.ProfileStorage
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ISubscriberTradesRVListPresenter
import ru.wintrade.mvp.view.item.SubscriberTradeItemView
import ru.wintrade.mvp.view.subscriber.SubscriberDealView
import ru.wintrade.navigation.Screens
import java.text.SimpleDateFormat
import javax.inject.Inject

class SubscriberDealPresenter : MvpPresenter<SubscriberDealView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profileStorage: ProfileStorage

    @Inject
    lateinit var apiRepo: ApiRepo

    val listPresenter = SubscriberTradesRVListPresenter()
    val traders = mutableListOf<Trader>()
    var newTradeDisposable: Disposable? = null

    inner class SubscriberTradesRVListPresenter : ISubscriberTradesRVListPresenter {
        val trades = mutableListOf<Trade>()

        override fun getCount() = trades.size

        override fun bind(view: SubscriberTradeItemView) {
            val trade = trades[view.pos]
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val date = dateFormat.format(trade.date)
            val sum = trade.price * trade.count
            traders.forEach {
                if (it.username == trade.trader)
                    it.avatar?.let { ava -> view.setAvatar(ava) }
            }
            view.setCompany(trade.company)
            view.setCount("Кол-во: ${trade.count}")
            view.setDate("Дата: $date")
            view.setNickname(trade.trader)
            view.setType(trade.operationType)
            view.setPrice("Цена: ${trade.price} ${trade.currency}")
            view.setSum("Сумма: $sum ${trade.currency}")
            trade.profitCount?.let {
                if (trade.profitCount.toFloat() >= 0) {
                    view.setProfit("${trade.profitCount}", Color.GREEN)
                } else {
                    view.setProfit("${trade.profitCount}", Color.RED)
                }
            } ?: view.setProfit("", Color.WHITE)
        }

        override fun clicked(pos: Int) {
            val trade = trades[pos]
            router.navigateTo(Screens.TradeDetailScreen(trade))
        }
    }

    fun refreshed() {
        loadData()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadData()
        apiRepo.newTradeSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(getNewTradeObserver())
    }

    override fun onDestroy() {
        super.onDestroy()
        newTradeDisposable?.dispose()
    }

    private fun loadData() {
        viewState.setRefreshing(true)
        Single.zip(apiRepo.getAllTradersSingle(),
            apiRepo.mySubscriptions(profileStorage.profile!!.token),
            BiFunction { allTraders, subs ->
                val subTraders = mutableListOf<Trader>()
                allTraders.forEach { trader ->
                    subs.forEach { sub ->
                        if (trader.username == sub.trader)
                            subTraders.add(trader)
                    }
                }
                traders.clear()
                traders.addAll(subTraders)
                subTraders
            }
        ).subscribe(
            { subTraders ->
                val tradeSingles = mutableListOf<Single<List<Trade>>>()

                subTraders.forEach { subTrader ->
                    tradeSingles.add(
                        apiRepo.getTradesByTrader(
                            profileStorage.profile!!.token,
                            subTrader.id
                        )
                    )
                }

                Single.zip(tradeSingles, Function<Array<Any>, List<Trade>> {
                    it.flatMap { it as List<Trade> }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        val sorted = it.sortedBy { trade -> trade.date }.reversed()
                        listPresenter.trades.clear()
                        listPresenter.trades.addAll(sorted)
                        viewState.updateAdapter()
                        viewState.setRefreshing(false)
                    },
                    {
                        it.printStackTrace()
                        viewState.setRefreshing(false)
                    }
                )
            },
            {}
        )
    }

    private fun getNewTradeObserver() = object: Observer<Boolean> {
        override fun onSubscribe(d: Disposable) {
            newTradeDisposable = d
        }

        override fun onNext(t: Boolean) {
            loadData()
        }

        override fun onError(e: Throwable) {

        }

        override fun onComplete() {

        }

    }
}