package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Comment(
    val id: Long,
    val postId: Long,
    var parentCommentId: Long?,
    val authorUuid: String,
    val authorUsername: String,
    val avatar: String,
    val text: String,
    val dateCreate: Date,
    val dateUpdate: Date,
    val likeCount: Int,
    val dislikeCount: Int
) : Parcelable