package ru.wintrade.mvp.view.traderme

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.traderme.TraderMeTradePresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderMeTradeView : MvpView {
    fun init()
    fun setBtnState(state: TraderMeTradePresenter.State)
    fun updateTradesAdapter()
    fun renderOperationsCount(operationsCount: Int)
}