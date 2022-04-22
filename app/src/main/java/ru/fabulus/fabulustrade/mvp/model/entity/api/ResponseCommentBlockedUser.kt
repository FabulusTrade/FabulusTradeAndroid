package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseCommentBlockedUser(
    @Expose
    @SerializedName("authorID")
    val authorID: String,

    @Expose
    @SerializedName("blockedUserID")
    val blockedUserID: String
)