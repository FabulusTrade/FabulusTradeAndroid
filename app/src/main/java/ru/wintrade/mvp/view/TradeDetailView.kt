package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TradeDetailView: MvpView {
    fun init()
    fun setName(traderName: String)
    fun setType(type: String)
    fun setCompany(company: String)
    fun setTicker(ticker: String)
    fun setPrice(price: String)
    fun setPriceTitle(priceTitle: String)
    fun setDate(date: String)
    fun setSubtype(type: String)
}