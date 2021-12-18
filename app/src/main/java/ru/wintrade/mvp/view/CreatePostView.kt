package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface CreatePostView : MvpView {
    fun init()
    fun setHintText(isPublication: Boolean, isPinnedEdit: Boolean?)
    fun showImagesAddedMessage(count: Int)
}