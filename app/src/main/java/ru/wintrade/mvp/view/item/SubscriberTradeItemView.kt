package ru.wintrade.mvp.view.item

interface SubscriberTradeItemView {
    var pos: Int

    fun setAvatar(url: String)
    fun setNickname(nickname: String)
    fun setType(type: String)
    fun setCompanyAndTicker(company: String, ticker: String)
    fun setPriceAndCurrency(price: Float, currency: String)
    fun setDate(date: String)
    fun setProfit(profit: String, color: Int)
}