package ru.wintrade.mvp.view.traderforsubscriber

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderForSubscriberMainView : MvpView {
    fun init()
    fun setSubscribeBtnActive(isActive: Boolean)
    fun setObserveVisibility(isVisible: Boolean)
    fun setObserveActive(isActive: Boolean)
}