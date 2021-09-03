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
    fun setPinnedTextVisible(isOpen: Boolean)
    fun setAverageDealsTime(dealsTime: String)
    fun setAverageDealsPositiveCountAndProfit(averageProfit: String)
    fun setAverageDealsNegativeCountAndProfit(averageProfit: String)
    fun setJanProfit(profit: String)
    fun setFebProfit(profit: String)
    fun setMarProfit(profit: String)
    fun setAprProfit(profit: String)
    fun setMayProfit(profit: String)
    fun setJunProfit(profit: String)
    fun setJulProfit(profit: String)
    fun setAugProfit(profit: String)
    fun setSepProfit(profit: String)
    fun setOctProfit(profit: String)
    fun setNovProfit(profit: String)
    fun setDecProfit(profit: String)
    fun showInfoDialog()
}