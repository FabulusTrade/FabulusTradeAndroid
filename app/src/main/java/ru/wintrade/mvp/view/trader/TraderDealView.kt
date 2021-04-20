package ru.wintrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.trader.TraderDealPresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderDealView : MvpView {
    fun init()
    fun setBtnState(state: TraderDealPresenter.State)
    fun updateRecyclerView()
}