package ru.wintrade.mvp.presenter.trader

import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.adapter.ITraderTradesRVListPresenter
import ru.wintrade.mvp.view.item.TraderTradeItemView
import ru.wintrade.mvp.view.trader.TraderDealView
import ru.wintrade.navigation.Screens
import java.text.SimpleDateFormat
import javax.inject.Inject

class TraderTradePresenter : MvpPresenter<TraderDealView>() {
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
    val listPresenter = TraderTradesRVListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        if (profile.user == null) {
            viewState.isAuthorized(false)
        } else {
            viewState.isAuthorized(true)
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
        router.navigateTo(Screens.SignInScreen())
    }

    fun openSignUpScreen() {
        router.navigateTo(Screens.SignUpScreen())
    }

    inner class TraderTradesRVListPresenter() : ITraderTradesRVListPresenter {
        val trades = mutableListOf<Trade>()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")

        override fun getCount() = trades.size

        override fun bind(view: TraderTradeItemView) {
            val trade = trades[view.pos]

            view.setCompanyLastTradeDate("Последняя сделка: ${dateFormat.format(trade.date)}")
            view.setCompanyName(trade.company)
            view.setCompanyLogo(trade.companyImg)
        }

        override fun clicked(pos: Int) {
        }

    }
}