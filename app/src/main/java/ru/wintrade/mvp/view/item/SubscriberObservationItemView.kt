package ru.wintrade.mvp.view.item

interface SubscriberObservationItemView {
    var pos: Int
    fun setTraderName(name: String)
    fun setTraderProfit(profit: String)
    fun setTraderAvatar(avatar: String)
}