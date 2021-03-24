package ru.wintrade.mvp.model.entity

data class Trade(
    val id: Long,
    val trader: String,
    val operationType: String,
    val company: String,
    val companyImg: String,
    val ticker: String,
    val orderStatus: String,
    val orderNum: Long,
    val price: Float,
    val count: Float,
    val currency: String,
    val date: String,
    val subtype: String,
    val profitCount: String
)