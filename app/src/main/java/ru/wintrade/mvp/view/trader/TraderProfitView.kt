package ru.wintrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.trader.TraderProfitPresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderProfitView : MvpView {
    fun init()
    fun setDateJoined(date: String)
    fun setPinnedTextVisible(isOpen: Boolean)
    fun setBtnsState(state: TraderProfitPresenter.State)
    fun setPinnedPostText(text: String?)
    fun setFollowersCount(followersCount: String)
    fun setTradesCount(tradesCount: String)
    fun setAverageDealsTime(dealsTime: String)
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