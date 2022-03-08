package ru.fabulus.fabulustrade.mvp.view.item

interface CompanyTradingOperationsJournalItemView {
    var pos: Int
    fun setOperationType(type: String)
    fun setCompanyLogo(url: String)
    fun setOperationDate(date: String)
    fun setProfitCount(profit: String?)
    fun setTradePrice(price: String)
    fun setEndCount(endCount: Int)
    fun setVisible(visible: Boolean)
}