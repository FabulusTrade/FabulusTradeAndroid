package ru.fabulus.fabulustrade.mvp.view.subscriber

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface SubscriberNewsView: MvpView {
    fun init()
    fun updateAdapter()
    fun withoutSubscribeAnyTrader()
}