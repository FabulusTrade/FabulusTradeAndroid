package ru.fabulus.fabulustrade.mvp.view

import android.content.Intent
import android.text.Spanned
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PostDetailView : MvpView {
    fun init()
    fun setPostAuthorAvatar(avatarUrl: String)
    fun setPostAuthorName(authorName: String)
    fun setPostDateCreated(dateCreatedString: String)
    fun setPostText(text: String)
    fun setProfit(profit: String, textColor: Int)
    fun setProfitNegativeArrow()
    fun setProfitPositiveArrow()
    fun setPostImages(images: List<String>?)
    fun setPostLikeCount(likeCount: String)
    fun setPostDislikeCount(dislikeCount: String)
    fun setLikeActiveImage()
    fun setLikeInactiveImage()
    fun setDislikeActiveImage()
    fun setDislikeInactiveImage()
    fun share(shareIntent: Intent)
    fun setCommentCount(text: String)
    fun updateCommentsAdapter()
    fun setRvPosition(position: Int)
    fun setCurrentUserAvatar(avatarUrl: String)
    fun setClickableSendCommentBtn()
    fun setUnclickableSendCommentBtn()
    fun clearNewCommentText()
    fun prepareReplyToComment(text: Spanned, maxCommentLength: Int)
    fun prepareUpdateComment(text: Spanned, maxCommentLength: Int)
    fun showToast(text: String)
    fun showComplainSnackBar()
    fun scrollNsvCommentViewToBottom()
    fun setAuthorFollowerCount(text: String)
    fun setClickableUpdateCommentBtn()
    fun setUnclickableUpdateCommentBtn()
    fun setIncItemSendCommentVisibility(visibility: Int)
    fun setIncItemUpdateCommentVisibility(visibility: Int)
    fun showSendComment(maxCommentLength: Int)
    fun showUpdateComment(maxCommentLength: Int)
    fun notifyItemChanged(position: Int)
    fun setMaxSendCommentLength(maxLength: Int)
    fun setMaxUpdateCommentLength(maxLength: Int)
    fun setRepostCount(text: String)
    fun setKebabMenuSelf()
    fun setKebabMenuSomeone()
    fun setUpdateData(text: String)
}