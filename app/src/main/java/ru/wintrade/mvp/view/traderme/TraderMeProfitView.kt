package ru.wintrade.mvp.view.traderme

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderMeProfitView : MvpView {
    fun init()
    fun setDateJoined(date: String)
    fun setFollowersCount(followersCount: Int)
    fun setTradesCount(tradesCount: Int)
    fun setPinnedPostText(text: String?)
//    fun setFollowersCountForWeek(countForWeek: Int)
}