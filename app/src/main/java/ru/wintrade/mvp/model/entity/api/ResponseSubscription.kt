package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import ru.wintrade.mvp.model.entity.Trader

data class ResponseSubscription(
    @Expose
    val id_trader: Trader,
    @Expose
    val end_date: String?,
    @Expose
    val id_status: Long?
)