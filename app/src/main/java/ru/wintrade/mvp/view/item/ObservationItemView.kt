package ru.wintrade.mvp.view.item

interface ObservationItemView {
    var pos: Int
    fun setTraderName(name: String)
    fun setTraderProfit(profit: String, textColor: Int)
    fun setTraderAvatar(avatar: String?)
    fun subscribeStatus(isSubscribe: Boolean)
}