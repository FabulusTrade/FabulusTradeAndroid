package ru.fabulus.fabulustrade.mvp.view.item

interface BlacklistItemView {
    var pos: Int
    fun setTraderName(name: String)
    fun setTraderProfit(profit: String, textColor: Int)
    fun setTraderAvatar(avatar: String?)
}