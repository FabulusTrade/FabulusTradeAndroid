package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class RequestSubscription(
    @Expose
    val id_trader: Long,
    @Expose
    val end_date: String?
)