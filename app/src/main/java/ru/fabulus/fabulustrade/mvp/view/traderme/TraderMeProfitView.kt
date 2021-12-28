package ru.fabulus.fabulustrade.mvp.view.traderme

import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMeProfitPresenter
import ru.fabulus.fabulustrade.mvp.view.base.BaseTraderStatisticView

@StateStrategyType(AddToEndStrategy::class)
interface TraderMeProfitView : BaseTraderStatisticView {
    fun setBtnsState(state: TraderMeProfitPresenter.State)
}