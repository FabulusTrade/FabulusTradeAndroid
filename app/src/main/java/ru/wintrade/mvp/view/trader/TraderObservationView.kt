package ru.wintrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderObservationView: MvpView {
    fun init()
}