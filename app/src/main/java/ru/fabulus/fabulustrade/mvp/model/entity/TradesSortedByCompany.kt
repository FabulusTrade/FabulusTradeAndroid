package ru.fabulus.fabulustrade.mvp.model.entity

import java.util.Date

data class TradesSortedByCompany(
    val id: Long,
    val operationType: String,
    val company: String,
    val companyImg: String,
    val date: Date,
    val profitCount: Double?,
    val price: Float,
    val currency: String,
    val posts: Int?
)