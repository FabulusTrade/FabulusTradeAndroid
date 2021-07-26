package ru.wintrade.mvp.model.entity.api

import android.graphics.Bitmap
import com.google.gson.annotations.Expose
import okhttp3.MultipartBody

data class RequestCreatePost(
    @Expose
    val trader_id: String,
    @Expose
    val text: String,
    @Expose
    val post_status: String = "Pub",
    @Expose
    val pinned: Boolean,
    @Expose
    val image: MultipartBody.Part?
)