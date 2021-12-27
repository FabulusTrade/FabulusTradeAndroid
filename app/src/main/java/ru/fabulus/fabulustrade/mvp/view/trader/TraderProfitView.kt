package ru.fabulus.fabulustrade.mvp.view.trader

import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.presenter.trader.TraderProfitPresenter
import ru.fabulus.fabulustrade.mvp.view.base.BaseTraderStatisticView

@StateStrategyType(AddToEndStrategy::class)
interface TraderProfitView : BaseTraderStatisticView {
    fun setBtnsState(state: TraderProfitPresenter.State)
}