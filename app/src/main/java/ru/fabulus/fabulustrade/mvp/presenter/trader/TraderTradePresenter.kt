package ru.fabulus.fabulustrade.mvp.presenter.trader

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.model.entity.TradesByCompanyAggregated
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.ITradesByCompanyListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.TradesByCompanyItemView
import ru.fabulus.fabulustrade.mvp.view.trader.TraderDealView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.toStringFormat

import javax.inject.Inject

class TraderTradePresenter(val trader: Trader) : MvpPresenter<TraderDealView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private var isLoading = false
    private var nextPage: Int? = 1

    enum class State {
        MY_DEALS, MY_ORDERS, MY_JOURNAL
    }

    private var state = State.MY_DEALS
    val listPresenter = TraderTradesRVListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        if (profile.user == null) {
            viewState.isAuthorized(false)
        } else {
            viewState.isAuthorized(true)
            loadTrades()
            loadTraderOperationsCount()
        }
    }

    private fun loadTraderOperationsCount() {
        apiRepo.getTraderOperationsCount(trader.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ operationsCount ->
                viewState.renderOperationsCount(operationsCount)
            }, { error ->
                // Ошибка не обрабатывается
            })
    }

    private fun loadTrades() {
        profile.token?.let {
            apiRepo
                .getTraderTradesAggregate(it, trader.id, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    listPresenter.trades.addAll(pag.results)
                    viewState.updateRecyclerView()
                    nextPage = pag.next
                }, {
                    // Ошибка не обрабатывается
                })
        }
    }

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

    fun openSignInScreen() {
        router.navigateTo(Screens.signInScreen(false))
    }

    fun openSignUpScreen() {
        router.navigateTo(Screens.signUpScreen(false))
    }

    inner class TraderTradesRVListPresenter : ITradesByCompanyListPresenter {
        val trades = mutableListOf<TradesByCompanyAggregated>()

        override fun getCount() = trades.size

        override fun bind(view: TradesByCompanyItemView) {
            val trade = trades[view.pos]

            view.setLastTradeTime(
                "${resourceProvider.getStringResource(R.string.last_operation)} ${
                    trade.lastTrade?.toStringFormat()
                }"
            )
            view.setCompanyName(trade.companyName)
            view.setCompanyLogo(trade.companyLogo)
            view.setTradesCount(
                "${resourceProvider.getStringResource(R.string.count_operations)} ${
                    trade.tradesCount
                }"
            )
        }

        override fun onItemClick(view: TradesByCompanyItemView) {
            router.navigateTo(
                Screens.companyTradingOperationsScreen(trader.id, trades[view.pos].companyId)
            )
        }
    }

    fun onScrollLimit() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            profile.token?.let {
                apiRepo
                    .getTraderTradesAggregate(it, trader.id, nextPage!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ pag ->
                        listPresenter.trades.addAll(pag.results)
                        viewState.updateRecyclerView()
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