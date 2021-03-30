package ru.wintrade.mvp.view.subscriber

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface SubscriberMainView : MvpView {
    fun init()
    fun setAvatar(ava: String?)
    fun setName(username: String)
    fun setSubscriptionCount(count: Int)
}