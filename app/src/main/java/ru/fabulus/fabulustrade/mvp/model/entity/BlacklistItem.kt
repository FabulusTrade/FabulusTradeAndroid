package ru.fabulus.fabulustrade.mvp.model.entity

data class BlacklistItem (
    val trader: Trader,
    val endDate: String?,
    val status: Long?
)