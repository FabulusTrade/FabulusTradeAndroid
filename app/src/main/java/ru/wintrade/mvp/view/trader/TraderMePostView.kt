package ru.wintrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.traderme.TraderMePostPresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderMePostView : MvpView {
    fun init()
    fun setBtnsState(state: TraderMePostPresenter.State)
    fun updateAdapter()
}