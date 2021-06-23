package ru.wintrade.mvp.view.traderme

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.traderme.TraderMeSubTradePresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderMeSubTradeView : MvpView {
    fun init()
    fun setBtnState(state: TraderMeSubTradePresenter.State)
    fun updateAdapter()
    fun setRefreshing(isRefreshing: Boolean)
}