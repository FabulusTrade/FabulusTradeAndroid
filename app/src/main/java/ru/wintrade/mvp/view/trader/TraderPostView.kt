package ru.wintrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.trader.TraderPostPresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderPostView : MvpView {
    fun init()
    fun setBtnsState(state: TraderPostPresenter.State)
    fun updateAdapter()
}