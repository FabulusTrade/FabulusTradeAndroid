package ru.wintrade.mvp.view.item

interface TradesByCompanyItemView {
    var pos: Int
    fun setCompanyLogo(image: String)
    fun setTradesCount(count: String)
    fun setCompanyName(name: String)
    fun setLastTradeTime(time: String)
}