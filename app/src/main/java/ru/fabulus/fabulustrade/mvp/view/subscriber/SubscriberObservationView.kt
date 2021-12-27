package ru.fabulus.fabulustrade.mvp.view.subscriber

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface SubscriberObservationView : MvpView {
    fun init()
    fun updateAdapter()
    fun withoutSubscribeAnyTrader()
    @StateStrategyType(SkipStrategy::class)
    fun showToast(msg: String)
}