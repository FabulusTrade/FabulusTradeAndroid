package ru.wintrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderProfitView : MvpView {
    fun init()
    fun setDateJoined(date: String)
    fun setFollowersCount(followersCount: Int)
    fun setTradesCount(tradesCount: Int)
//    fun setFollowersCountForWeek(countForWeek: Int)
}