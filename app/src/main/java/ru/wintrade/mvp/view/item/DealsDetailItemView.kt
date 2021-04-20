package ru.wintrade.mvp.view.item

import java.util.*

interface DealsDetailItemView {
    var pos: Int
    fun setLastDealDate(date: Date)
}