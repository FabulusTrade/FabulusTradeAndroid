package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponseSubscription(
    @Expose
    val id_trader: ResponseTrader,
    @Expose
    val end_date: String?,
    @Expose
    val id_status: Long?
)