package ru.wintrade.mvp.view.registration.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface RegAsTraderFourView : MvpView {
    fun init()
}