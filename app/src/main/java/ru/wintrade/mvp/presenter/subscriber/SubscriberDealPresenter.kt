package ru.wintrade.mvp.presenter.subscriber

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
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
            view.setCount("Кол-во ${trade.count}")
            view.setDate("Дата: $date")
            view.setNickname(trade.trader)
            view.setType(trade.operationType)
            view.setPrice("Цена: ${trade.price} ${trade.currency}")
            view.setSum("Сумма: $sum ${trade.currency}")
        }

        override fun clicked(pos: Int) {
            val trade = trades[pos]
            router.navigateTo(Screens.TradeDetailScreen(trade))
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
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
                    },
                    {
                        it.printStackTrace()
                    }
                )
            },
            {}
        )
    }
}