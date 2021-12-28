package ru.fabulus.fabulustrade.mvp.view.item

interface TraderTradeItemView {
    var pos: Int

    fun setCompanyName(name: String)
    fun setCompanyLogo(url: String)
    fun setCompanyTradeCount(count: String)
    fun setCompanyLastTradeDate(date: String)
}