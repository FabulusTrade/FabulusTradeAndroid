package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Comment(
    val id: Long,
    val postId: Long,
    var parentCommentId: Long?,
    var authorUuid: String?,
    var authorUsername: String?,
    var avatarUrl: String?,
    val text: String,
    val dateCreate: Date,
    val dateUpdate: Date,
    var likeCount: Int,
    val dislikeCount: Int,
    var isLiked: Boolean?
) : Parcelable {

    fun like() {
        isLiked = true
        likeCount++
    }

    fun unlike() {
        isLiked = false
        likeCount--
    }
}