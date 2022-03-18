package ru.fabulus.fabulustrade.mvp.view.subscriber

import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.view.base.BaseTraderView

@StateStrategyType(AddToEndStrategy::class)
interface SubscriberMainView : BaseTraderView {
    fun init()
    fun setName(username: String)
    fun setSubscriptionCount(count: Int)
}