package ru.wintrade.mvp.view.item

interface AllTradersItemView {
    var pos: Int
    fun setTraderFio(fio: String)
    fun setTraderFollowerCount(followerCount: Int)
}