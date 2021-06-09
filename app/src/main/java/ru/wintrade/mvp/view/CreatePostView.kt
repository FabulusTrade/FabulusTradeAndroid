package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface CreatePostView : MvpView {
    fun init()
    fun setHintText(isPinnedEdit: Boolean?)
    @StateStrategyType(SkipStrategy::class)
    fun pickImages()
}