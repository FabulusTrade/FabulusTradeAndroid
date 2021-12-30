package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class ResponseAddComment(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("post")
    val postId: Int,
    @Expose
    @SerializedName("parent_comment")
    val parentCommentId: Int?,
    @Expose
    @SerializedName("text")
    val text: String,
    @Expose
    @SerializedName("date_create")
    val dateCreate: Date,
    @Expose
    @SerializedName("date_update")
    val dateUpdate: Date
)