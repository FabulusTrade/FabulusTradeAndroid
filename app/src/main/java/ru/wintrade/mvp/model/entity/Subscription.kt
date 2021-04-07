package ru.wintrade.mvp.model.entity

data class Subscription (
    val id_trader: Trader,
    val end_date: String?,
    val id_status: Long?
)