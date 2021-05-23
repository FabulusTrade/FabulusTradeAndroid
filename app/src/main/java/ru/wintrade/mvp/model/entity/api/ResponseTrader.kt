package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponseTrader(
    @Expose
    val id: String,
    @Expose
    val username: String,
    @Expose
    val avatar: String,
    @Expose
    val kval: Boolean,
    @Expose
    val is_trader: Boolean,
    @Expose
    val is_active: Boolean,
    @Expose
    val date_joined: String,
    @Expose
    val followers_count: Int,
    @Expose
    val trades_count: Int,
    @Expose
    val year_profit: String,
    @Expose
    val followers_count_7day: Int,
    @Expose
    val pinned_post: ResponsePost?
)