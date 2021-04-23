package ru.wintrade.mvp.view.item

import java.util.*

interface CompanyTradingOperationsItemView {
    var pos: Int
    fun setLastOperationDate(date: Date)
}