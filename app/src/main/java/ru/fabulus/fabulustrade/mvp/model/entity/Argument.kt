package ru.fabulus.fabulustrade.mvp.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Argument(
    val id: Int,
    val userName: String,
    val avatarUrl: String,
    val followersCount: Int,
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
    var comments: MutableList<Comment>,
    val colorIncrDecrDepo365: ColorItem,
    var repostCount: Int,
    var isFlashed: Boolean,
    var stopLoss: Float?,
    var takeProfit: Float?,
    var dealTerm: Float?,
    var closedDealTerm: Float?
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

    fun incRepostCount() {
        repostCount += 1
    }
}