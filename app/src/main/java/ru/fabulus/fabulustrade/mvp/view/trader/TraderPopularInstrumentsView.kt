package ru.fabulus.fabulustrade.mvp.view.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderPopularInstrumentsView : MvpView {
    fun init()
    fun isAuthorized(isAuth: Boolean)
}