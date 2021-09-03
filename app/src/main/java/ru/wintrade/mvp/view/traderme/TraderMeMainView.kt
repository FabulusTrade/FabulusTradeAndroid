package ru.wintrade.mvp.view.traderme

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.model.entity.TraderStatistic

@StateStrategyType(AddToEndStrategy::class)
interface TraderMeMainView: MvpView {
    fun init()
    fun setProfit(profit: String, isPositive: Boolean)
    fun setUsername(username: String)
    fun setSubscriberCount(count: Int)
    fun setAvatar(url: String?)
    fun initVP(traderStatistic: TraderStatistic)
}