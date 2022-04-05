package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseSetFlashedPost(
    @Expose
    @SerializedName("result")
    val result: String,
    @Expose
    @SerializedName("flash_limit")
    val flashLimit: Int?,
    @Expose
    @SerializedName("message")
    val message: String?
)