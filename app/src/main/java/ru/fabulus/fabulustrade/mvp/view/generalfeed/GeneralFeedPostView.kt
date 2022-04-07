package ru.fabulus.fabulustrade.mvp.view.generalfeed

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface GeneralFeedPostView : MvpView {
    fun init()
}