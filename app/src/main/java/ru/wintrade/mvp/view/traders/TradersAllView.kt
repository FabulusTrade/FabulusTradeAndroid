package ru.wintrade.mvp.view.traders

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TradersAllView : MvpView {
    fun init()
    fun updateAdapter()
}