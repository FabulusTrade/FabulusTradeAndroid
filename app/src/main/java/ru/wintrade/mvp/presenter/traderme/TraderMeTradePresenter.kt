package ru.wintrade.mvp.presenter.traderme

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.TradesByCompanyAggregated
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITradesByCompanyListPresenter
import ru.wintrade.mvp.view.item.TradesByCompanyItemView
import ru.wintrade.mvp.view.traderme.TraderMeTradeView
import ru.wintrade.navigation.Screens
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TraderMeTradePresenter : MvpPresenter<TraderMeTradeView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    private var isLoading = false
    private var nextPage: Int? = 1

    enum class State {
        MY_DEALS, MY_ORDERS, MY_JOURNAL
    }

    private var state = State.MY_DEALS

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

    val listPresenter = TraderTradesRVListPresenter()

    inner class TraderTradesRVListPresenter : ITradesByCompanyListPresenter {
        val trades = mutableListOf<TradesByCompanyAggregated>()
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        override fun getCount() = trades.size

        override fun bind(view: TradesByCompanyItemView) {
            val trade = trades[view.pos]

            view.setLastTradeTime("Последняя сделка: ${dateFormat.format(trade.lastTrade)}")
            view.setCompanyName(trade.companyName)
            view.setCompanyLogo(trade.companyLogo)
            view.setTradesCount("Количество сделок: ${trade.tradesCount}")
        }

        override fun onItemClick(view: TradesByCompanyItemView) {
            router.navigateTo(
                Screens.CompanyTradingOperationsScreen(
                    profile.user!!.id,
                    trades[view.pos].companyId
                )
            )
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setBtnState(State.MY_DEALS)
        loadTrades()
    }

    private fun loadTrades() {
        profile.token?.let {
            apiRepo.getTraderTradesAggregate(it, profile.user!!.id, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ pag ->
                    listPresenter.trades.addAll(pag.results)
                    viewState.updateTradesAdapter()
                    nextPage = pag.next
                }, {})
        }
    }

    fun onScrollLimit() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            profile.token?.let {
                apiRepo.getTraderTradesAggregate(it, profile.user!!.id, nextPage!!)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ pag ->
                        listPresenter.trades.addAll(pag.results)
                        viewState.updateTradesAdapter()
                        nextPage = pag.next
                        isLoading = false
                    }, {
                        it.printStackTrace()
                        isLoading = false
                    })
            }
        }
    }
}