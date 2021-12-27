package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.fabulus.fabulustrade.mvp.model.entity.ColorItem

data class ResponsePost(
    @Expose
    val id: Int,
    @Expose
    val username: String,
    @Expose
    val avatar: String,
    @Expose
    val trader_id: String,
    @Expose
    val text: String,
    @Expose
    val post_status: String,
    @Expose
    val date_create: String,
    @Expose
    val date_update: String?,
    @Expose
    val pinned: Boolean,
    @Expose
    val images: List<ResponseImage>,
    @Expose
    val like_count: Int,
    @Expose
    val dislike_count: Int,
    @Expose
    val is_liked: Boolean,
    @Expose
    val is_disliked: Boolean,
    @Expose
    val comments: List<ResponseComment>,
    @Expose
    @SerializedName("color_incr_decr_depo_365")
    val colorIncrDecrDepo365: ColorItem
)