package ru.wintrade.mvp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Traders(
    @Expose
    @field:SerializedName("fio") val fio: String,
    @Expose
    @field:SerializedName("followers_count") val fCount: Int
)