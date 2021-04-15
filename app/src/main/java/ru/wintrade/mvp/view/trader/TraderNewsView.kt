package ru.wintrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.trader.TraderNewsPresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderNewsView : MvpView {
    fun init()
    fun setBtnsState(state: TraderNewsPresenter.State)
    fun updateRecyclerView()
    fun setVisibility(isVisible: Boolean)
}