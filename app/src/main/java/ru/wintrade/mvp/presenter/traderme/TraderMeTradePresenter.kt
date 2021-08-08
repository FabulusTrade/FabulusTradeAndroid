package ru.wintrade.mvp.presenter.traderme

import android.annotation.SuppressLint
import android.graphics.Color
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ISubscriberTradesRVListPresenter
import ru.wintrade.mvp.view.item.SubscriberTradeItemView
import ru.wintrade.mvp.view.traderme.TraderMeTradeView
import ru.wintrade.navigation.Screens
import java.text.SimpleDateFormat
import javax.inject.Inject

class TraderMeTradePresenter : MvpPresenter<TraderMeTradeView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    enum class State {
        MY_DEALS, MY_ORDERS, MY_JOURNAL
    }

    private var state = State.MY_DEALS

//    val tradesListPresenter = TradesByCompanyRVListPresenter()
//    val ordersListPresenter = TradesByCompanyRVListPresenter()
//
//    inner class TradesByCompanyRVListPresenter: ITradesByCompanyListPresenter {
//        val tradesByCompany = mutableListOf<TradesByCompany>()
//
//        override fun getCount() = tradesByCompany.size
//
//        override fun bind(view: TradesByCompanyItemView) {
//            val company = tradesByCompany[view.pos]
//            view.setCompanyLogo(company.logo)
//            view.setCompanyName(company.name)
//            view.setLastTradeTime(company.lastTime)
//            view.setTradesCount(company.count)
//        }
//    }
//
//    override fun onFirstViewAttach() {
//        super.onFirstViewAttach()
//        viewState.init()
//    }

    fun myDealsBtnClicked() {
        state = State.MY_DEALS
        viewState.setBtnState(state)
    }

    fun myOrdersBtnClicked() {
        state = State.MY_ORDERS
        viewState.setBtnState(state)
    }

    fun myJournalBtnClicked() {
        state = State.MY_JOURNAL
        viewState.setBtnState(state)
    }


    private var nextPage: Int? = 1
    val listPresenter = SubscriberTradesRVListPresenter()
    val traders = mutableListOf<Trader>()

    inner class SubscriberTradesRVListPresenter : ISubscriberTradesRVListPresenter {
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

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setBtnState(State.MY_DEALS)
        apiRepo.getMyTrades(profile.token!!, nextPage!!).observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    listPresenter.trades.clear()
                    listPresenter.trades.addAll(it.results)
                    viewState.updateTradesAdapter()
                },
                {}
            )
    }
}