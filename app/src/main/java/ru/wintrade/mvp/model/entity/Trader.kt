package ru.wintrade.mvp.model.entity

data class Trader (
    val id: Long,
    val username: String,
    val email: String,
    val avatar: String,
    val kval: Boolean,
    val isTrader: Boolean,
    val isActive: Boolean,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val dateJoined: String,
    val phone: String?,
    val tradesCount: Int,
    val fio: String,
    val followersCount: Int,
    val yearProfit: String
)