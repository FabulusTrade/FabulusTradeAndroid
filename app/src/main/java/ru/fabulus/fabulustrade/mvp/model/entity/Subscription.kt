package ru.fabulus.fabulustrade.mvp.model.entity

data class Subscription (
    val trader: Trader,
    val endDate: String?,
    val status: Long?
)