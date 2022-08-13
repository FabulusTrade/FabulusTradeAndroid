package ru.fabulus.fabulustrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.model.entity.Argument
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.ui.fragment.TradeDetailFragment

@StateStrategyType(AddToEndSingleStrategy::class)
interface TradeArgumentView  : BasePostView, MvpView {
    fun setRepostCount(text: String)
    fun setPostMenuSelf(argument: Argument)
    fun setPostMenuSomeone(argument: Argument, complaintList: List<Complaint>)
    fun setType(type: String)
    fun setCompany(company: String)
    fun setTicker(ticker: String)
    fun setPrice(price: String)
    fun setPriceTitle(priceTitle: String)
    fun setDate(date: String)
    fun setSubtype(type: String)
    fun setTradeType(type: TradeDetailFragment.TradeType)
    fun setTakeProfit(takeProfit: Float)
    fun setStopLoss(stopLoss: Float)
    fun setProfit(profit: Float, precision: Int)
    fun setLoss(loss: Float, precision: Int)
    fun setDealTerm(term: Double, precision: Int)
    fun setDealTerm(term: Int)
    fun relocateKebabForOpeningTrade()
}