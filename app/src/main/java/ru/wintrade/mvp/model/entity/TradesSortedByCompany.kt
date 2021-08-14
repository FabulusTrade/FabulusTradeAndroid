package ru.wintrade.mvp.model.entity

import java.util.*

data class TradesSortedByCompany(
    val id: Long,
    val operationType: String,
    val company: String,
    val companyImg: String,
    val date: Date,
    val profitCount: String?,
    val price: Float,
    val currency: String
)