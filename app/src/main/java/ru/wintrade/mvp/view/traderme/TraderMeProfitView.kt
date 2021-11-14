package ru.wintrade.mvp.view.traderme

import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.traderme.TraderMeProfitPresenter
import ru.wintrade.mvp.view.base.BaseTraderStatisticView

@StateStrategyType(AddToEndStrategy::class)
interface TraderMeProfitView : BaseTraderStatisticView {
    fun setBtnsState(state: TraderMeProfitPresenter.State)
}