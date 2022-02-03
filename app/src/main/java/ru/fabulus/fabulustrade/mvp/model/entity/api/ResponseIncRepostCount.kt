package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseIncRepostCount(
    @Expose
    @SerializedName("result")
    val result: String,
    @Expose
    @SerializedName("message")
    val message: String
)