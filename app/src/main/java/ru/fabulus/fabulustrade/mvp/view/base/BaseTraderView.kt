package ru.fabulus.fabulustrade.mvp.view.base

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface BaseTraderView : MvpView {
    fun setAvatar(url: String?)
}