package ru.wintrade.mvp.view.item

import java.util.*

interface PostItemView {
    var pos: Int
    fun setNewsDate(date: Date)
    fun setPost(text: String)
    fun setLikesCount(likes: Int)
    fun setDislikesCount(dislikesCount: Int)
    fun setImages(images: List<String>?)
    fun setLikeImage(isLiked: Boolean)
    fun setDislikeImage(isDisliked: Boolean)
    fun setKebabMenuVisibility(isVisible: Boolean)
}