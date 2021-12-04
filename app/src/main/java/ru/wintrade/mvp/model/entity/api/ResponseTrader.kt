package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.wintrade.mvp.model.entity.ColorItem

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
    val incr_decr_depo_365: Double,
    @Expose
    @SerializedName("color_incr_decr_depo_365")
    val colorIncrDecrDepo365: ColorItem?,
    @Expose
    val followers_count_7day: Int,
    @Expose
    val pinned_post: ResponsePost?
)