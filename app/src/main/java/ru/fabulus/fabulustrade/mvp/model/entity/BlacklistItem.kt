package ru.fabulus.fabulustrade.mvp.model.entity

data class BlacklistItem(
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val blacklistedAt: String,
    val yearProfit: Float,
    val followersCount: Int
)