package ru.wintrade.mvp.view

import android.content.Intent
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
    fun sharePost(shareIntent: Intent)
}