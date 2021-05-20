package ru.wintrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderPostView: MvpView {
    fun init()
    fun updateAdapter()
}