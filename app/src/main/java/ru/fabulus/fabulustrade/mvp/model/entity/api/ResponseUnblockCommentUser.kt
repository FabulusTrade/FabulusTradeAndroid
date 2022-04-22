package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseUnblockCommentUser(
    @Expose
    @SerializedName("result")
    val result: String
)
