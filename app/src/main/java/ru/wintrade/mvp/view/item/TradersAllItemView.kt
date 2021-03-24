package ru.wintrade.mvp.view.item

interface TradersAllItemView {
    var pos: Int
    fun setTraderFio(fio: String)
    fun setTraderFollowerCount(followerCount: Int)
}