package ru.fabulus.fabulustrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.model.entity.Post

@StateStrategyType(AddToEndSingleStrategy::class)
interface PostDetailView  : BasePostView, MvpView {
    fun setRepostCount(text: String)
    fun setPostMenuSelf(post: Post)
    fun setPostMenuSomeone(post: Post)
    fun setFlashVisibility(isVisible:Boolean)
    fun setProfitAndFollowersVisibility(isVisible:Boolean)
}