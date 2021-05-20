package ru.wintrade.mvp.view.traderme

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderMeMainView: MvpView {
    fun init()
    fun setProfit(profit: String, isPositive: Boolean)
    fun setUsername(username: String)
    fun setSubscriberCount(count: Int)
    fun setAvatar(url: String)
}