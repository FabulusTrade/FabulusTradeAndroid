package ru.wintrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.trader.TraderTradePresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderDealView : MvpView {
    fun init()
    fun setBtnState(state: TraderTradePresenter.State)
    fun updateRecyclerView()
    fun isAuthorized(isAuth: Boolean)
}