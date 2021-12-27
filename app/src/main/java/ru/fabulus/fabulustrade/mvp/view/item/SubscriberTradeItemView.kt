package ru.fabulus.fabulustrade.mvp.view.item

interface SubscriberTradeItemView {
    var pos: Int

    fun setAvatar(url: String)
    fun setNickname(nickname: String)
    fun setType(type: String)
    fun setCompany(company: String)
    fun setPrice(price: String)
    fun setDate(date: String)
    fun setProfit(profit: String, color: Int)
    fun setProfitVisibility(visibility: Int)
}