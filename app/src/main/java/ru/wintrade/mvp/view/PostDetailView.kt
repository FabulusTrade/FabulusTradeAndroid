package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PostDetailView : MvpView {
    fun setPostAuthorAvatar(avatarUrl: String)
    fun setPostAuthorName(authorName: String)
    fun setPostDateCreated(dateCreatedString: String)
    fun setPostText(text: String)
}