package ru.fabulus.fabulustrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.presenter.trader.TraderTradePresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderDealView : MvpView {
    fun init()
    fun setBtnState(state: TraderTradePresenter.State)
    fun updateRecyclerView()
    fun isAuthorized(isAuth: Boolean)
    fun renderOperationsCount(operationsCount: Int)
}