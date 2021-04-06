package ru.wintrade.mvp.model.entity

data class Trader(
    val id: Long,
    val username: String?,
    val avatar: String?,
    val kval: Boolean,
    val isActive: Boolean,
    val isTrader: Boolean,
    val dateJoined: String?,
    val followersCount: Int,
    val tradesCount: Int,
    val yearProfit: String?,
    val followersForWeekCount: Int
)