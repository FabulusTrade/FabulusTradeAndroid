package ru.wintrade.mvp.view.subscriber

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface SubscriberObservationView : MvpView {
    fun init()
    fun updateAdapter()
    fun withoutSubscribeAnyTrader()
    fun showToast(msg: String)
}