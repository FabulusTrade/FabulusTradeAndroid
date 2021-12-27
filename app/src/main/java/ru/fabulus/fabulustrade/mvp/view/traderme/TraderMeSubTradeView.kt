package ru.fabulus.fabulustrade.mvp.view.traderme

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMeSubTradePresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderMeSubTradeView : MvpView {
    fun init(position: Int)
    fun setBtnState(state: TraderMeSubTradePresenter.State)
    fun updateAdapter()
    fun setRefreshing(isRefreshing: Boolean)
}