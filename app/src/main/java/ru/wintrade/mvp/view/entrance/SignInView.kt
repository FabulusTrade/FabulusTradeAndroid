package ru.wintrade.mvp.view.entrance

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SignInView : MvpView {
    fun init()
    fun setAccess(isAuthorized: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(toast: String)
}