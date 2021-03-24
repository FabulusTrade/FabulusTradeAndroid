package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseTrader(
    @Expose
    val id: Long,
    @Expose
    val username: String,
    @Expose
    val email: String,
    @Expose
    val avatar: String,
    @Expose
    val kval: Boolean,
    @Expose
    val is_trader: Boolean,
    @Expose
    val is_active: Boolean,
    @Expose
    val first_name: String,
    @Expose
    val last_name: String,
    @Expose
    val patronymic: String,
    @Expose
    val date_joined: String,
    @Expose
    val phone: String,
    @Expose
    val trades_count: Int,
    @Expose
    val fio: String,
    @Expose
    val followers_count: Int
)