package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderPopularInstrumentsView : MvpView {
    fun init()
}