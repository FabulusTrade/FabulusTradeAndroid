package ru.wintrade.mvp.model.entity

data class Subscription (
    val trader: Trader,
    val endDate: String?,
    val status: Long?
)