package ru.wintrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderMainView : MvpView {
    fun init()
    fun setSubscribeBtnActive(isActive: Boolean)
    fun setObserveVisibility(isVisible: Boolean)
    fun setObserveActive(isActive: Boolean)
    fun setUsername(username: String)
    fun setProfit(profit: String, isPositive: Boolean)
    fun setAvatar(avatar: String)
}