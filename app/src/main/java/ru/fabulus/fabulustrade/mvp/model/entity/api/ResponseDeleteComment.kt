package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseDeleteComment(
    @Expose
    @SerializedName("Result")
    val result: String
)