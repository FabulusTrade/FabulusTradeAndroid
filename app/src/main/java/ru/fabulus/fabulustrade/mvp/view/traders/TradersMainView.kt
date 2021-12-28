package ru.fabulus.fabulustrade.mvp.view.traders

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TradersMainView : MvpView {
    fun init(filter: Int)
    fun setRegistrationBtnVisible(isVisible: Boolean)
}