package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun init()
    fun setupHeader(avatar: String?, username: String?)

    @StateStrategyType(SkipStrategy::class)
    fun exit()

    @StateStrategyType(SkipStrategy::class)
    fun setAccess(isAuthorized: Boolean)
}