package ru.wintrade.mvp.view.item

interface CompanyTradingOperationsItemView {
    var pos: Int
    fun setOperationType(type: String)
    fun setCompanyLogo(url: String)
    fun setOperationDate(date: String)
    fun setProfitCount(profit: String?)
    fun setTradePrice(price: String)
}