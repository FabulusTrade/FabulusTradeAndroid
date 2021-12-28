package ru.fabulus.fabulustrade.mvp.view.entrance

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SignInView : MvpView {
    fun init(isAsTraderRegistration: Boolean)
    fun setAccess(isAuthorized: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(toast: String)
    fun setAppToolbarMenuVisible(visible: Boolean)
}