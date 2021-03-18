package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiTrader(
    @Expose
    @field:SerializedName("fio") val fio: String,
    @Expose
    @field:SerializedName("followers_count") val followersCount: Int
)