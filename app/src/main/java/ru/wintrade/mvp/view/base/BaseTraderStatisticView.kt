package ru.wintrade.mvp.view.base

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.trader.TraderProfitPresenter

@StateStrategyType(AddToEndStrategy::class)
interface BaseTraderStatisticView: MvpView {
    fun init()
    fun setDateJoined(date: String)
    fun setPinnedTextVisible(isOpen: Boolean)
    fun setPinnedPostText(text: String?)
    fun setFollowersCount(followersCount: String)
    fun setTradesCount(tradesCount: String)
    fun setAverageDealsTime(dealsTime: String)
    fun setJanProfit(profit: String, textColor: Int)
    fun setFebProfit(profit: String, textColor: Int)
    fun setMarProfit(profit: String, textColor: Int)
    fun setAprProfit(profit: String, textColor: Int)
    fun setMayProfit(profit: String, textColor: Int)
    fun setJunProfit(profit: String, textColor: Int)
    fun setJulProfit(profit: String, textColor: Int)
    fun setAugProfit(profit: String, textColor: Int)
    fun setSepProfit(profit: String, textColor: Int)
    fun setOctProfit(profit: String, textColor: Int)
    fun setNovProfit(profit: String, textColor: Int)
    fun setDecProfit(profit: String, textColor: Int)
    fun setPositiveProfitPercentForTransactions(percent: String)
    fun setNegativeProfitPercentForTransactions(percent: String)
    fun setAverageProfitForDeal(percent: String)
    fun setAverageLoseForDeal(percent: String)
    fun setDepoValue(percent: String)
    fun setAllDealLong(percent: String)
    fun setAllDealShort(percent: String)
    fun setAvaregeTimeDealLong(daysCount: String)
    fun setAvaregeTimeDealShort(daysCount: String)
    fun setPercentOfProfitDealsLong(percent: String)
    fun setPercentOfProfitDealsShort(percent: String)
    fun setAvaregePercentForProfitDealLong(percent: String)
    fun setAvaregePercentForProfitDealShort(percent: String)
    fun setPercentOfLosingDealsLong(percent: String)
    fun setPercentOfLosingDealsShort(percent: String)
    fun setAvaregePercentForLosingDealLong(percent: String)
    fun setAvaregePercentForLosingDealShort(percent: String)
}