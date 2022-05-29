package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponseBlacklistItem(
    @Expose
    val user_id: String,
    @Expose
    val username: String,
    @Expose
    val avatar_url: String,
    @Expose
    val blacklisted_at: String,
    @Expose
    val year_profit: Float,
    @Expose
    val followers_count: Int
)