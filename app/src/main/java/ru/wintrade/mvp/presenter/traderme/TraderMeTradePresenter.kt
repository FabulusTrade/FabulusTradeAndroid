package ru.wintrade.mvp.presenter.traderme

import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.TradesByCompany
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITradesByCompanyListPresenter
import ru.wintrade.mvp.view.item.TradesByCompanyItemView
import ru.wintrade.mvp.view.traderme.TraderMeTradeView
import javax.inject.Inject

class TraderMeTradePresenter: MvpPresenter<TraderMeTradeView>() {
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

    val tradesListPresenter = TradesByCompanyRVListPresenter()
    val ordersListPresenter = TradesByCompanyRVListPresenter()

    inner class TradesByCompanyRVListPresenter: ITradesByCompanyListPresenter {
        val tradesByCompany = mutableListOf<TradesByCompany>()

        override fun getCount() = tradesByCompany.size

        override fun bind(view: TradesByCompanyItemView) {
            val company = tradesByCompany[view.pos]
            view.setCompanyLogo(company.logo)
            view.setCompanyName(company.name)
            view.setLastTradeTime(company.lastTime)
            view.setTradesCount(company.count)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
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
}