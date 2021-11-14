package ru.wintrade.mvp.view.trader

import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.trader.TraderProfitPresenter
import ru.wintrade.mvp.view.base.BaseTraderStatisticView

@StateStrategyType(AddToEndStrategy::class)
interface TraderProfitView : BaseTraderStatisticView {
    fun setBtnsState(state: TraderProfitPresenter.State)
}