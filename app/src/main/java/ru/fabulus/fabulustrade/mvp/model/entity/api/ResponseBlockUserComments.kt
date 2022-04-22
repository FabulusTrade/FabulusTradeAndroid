package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseBlockUserComments(
    @Expose
    @SerializedName("results")
    val usersList: List<ResponseCommentBlockedUser>
)
