package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class ResponseComment(
    @Expose
    @SerializedName("id")
    val id: Long,
    @Expose
    @SerializedName("post")
    val postId: Long,
    @Expose
    @SerializedName("parent_comment")
    val parentCommentId: Long?,
    @Expose
    @SerializedName("author")
    val authorUuid: String,
    @Expose
    @SerializedName("author_username")
    val authorUsername: String,
    @Expose
    @SerializedName("avatar")
    val avatarUrl: String,
    @Expose
    @SerializedName("text")
    val text: String,
    @Expose
    @SerializedName("date_create")
    val dateCreate: Date,
    @Expose
    @SerializedName("date_update")
    val dateUpdate: Date,
    @Expose
    @SerializedName("like_count")
    val likeCount: Int,
    @Expose
    @SerializedName("dislike_count")
    val dislikeCount: Int
)