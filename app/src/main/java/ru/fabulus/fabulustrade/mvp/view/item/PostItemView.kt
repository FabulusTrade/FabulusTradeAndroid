package ru.fabulus.fabulustrade.mvp.view.item

import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import java.util.Date

interface PostItemView {
    var pos: Int
    var isOpen: Boolean
    var countPostTextLine: Int
    fun setNewsDate(date: Date)
    fun setPost(text: String)
    fun setLikesCount(likes: Int)
    fun setDislikesCount(dislikesCount: Int)
    fun setImages(images: List<String>?)
    fun setLikeImage(isLiked: Boolean)
    fun setDislikeImage(isDisliked: Boolean)
    fun setPublicationItemTextMaxLines(isOpen: Boolean)
    fun setProfileName(profileName: String)
    fun setProfileAvatar(avatarUrlPath: String)
    fun setCommentCount(text: String)

    fun setProfit(profit: String, textColor: Int)
    fun setProfitNegativeArrow()
    fun setProfitPositiveArrow()
    fun setAuthorFollowerCount(text: String)
    fun setRepostCount(text: String)
    fun setIvAttachedKebabMenuSelf(post: Post)
    fun setIvAttachedKebabMenuSomeone(post: Post, complaintList: List<Complaint>)
    fun setFlashVisibility(isVisible: Boolean)
    fun initFlashFooterVisibility(isVisible: Boolean)
    fun setFlashColor(color: Int)
    fun initFlashFooterColor(color: Int)

    fun setProfitAndFollowersVisibility(isVisible: Boolean)
    fun getCountLineAndSetButtonVisibility()
}