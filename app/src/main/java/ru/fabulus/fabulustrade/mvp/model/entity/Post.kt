package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Post(
    val id: Int,
    val userName: String,
    val avatarUrl: String,
    val traderId: String,
    val text: String,
    val postStatus: String,
    val dateCreate: Date,
    val dateUpdate: Date?,
    val pinned: Boolean,
    val images: List<String>,
    var likeCount: Int,
    var dislikeCount: Int,
    var isLiked: Boolean,
    var isDisliked: Boolean,
    var comments: List<Comment>,
) : Parcelable {
    fun like() {
        if (isLiked)
            likeCount--
        else
            likeCount++
        isLiked = !isLiked
    }

    fun dislike() {
        if (isDisliked)
            dislikeCount--
        else
            dislikeCount++
        isDisliked = !isDisliked
    }

    fun commentCount() : Int = comments.size
}