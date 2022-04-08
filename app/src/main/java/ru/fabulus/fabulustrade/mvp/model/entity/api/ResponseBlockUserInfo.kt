package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class ResponseBlockUserInfo(
    @Expose
    @SerializedName("blockedUser_id")
    val blockedUserId: String,
    @Expose
    @SerializedName("commentsBlockTime")
    val commentsBlockTime: Date,
    @Expose
    @SerializedName("postsBlockTime")
    val postsBlockTime: Date
)
