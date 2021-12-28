package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class RequestSubscription(
    @Expose
    val id_trader: String,
    @Expose
    val end_date: String?
)