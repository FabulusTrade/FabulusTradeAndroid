package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponsePost(
    @Expose
    val id: Int,
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
    val images: List<ResponseImage>
)

