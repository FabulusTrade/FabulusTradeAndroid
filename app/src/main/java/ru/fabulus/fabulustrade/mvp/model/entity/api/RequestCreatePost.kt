package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class RequestCreatePost(
    @Expose
    val trader_id: String,
    @Expose
    val text: String,
    @Expose
    val post_status: String = "Pub",
    @Expose
    val pinned: Boolean,
)