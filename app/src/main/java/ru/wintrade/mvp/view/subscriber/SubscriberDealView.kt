package ru.wintrade.mvp.view.subscriber

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface SubscriberDealView : MvpView {
    fun init()
    fun updateAdapter()
    fun selectBtn(pos: Int)
    fun setRefreshing(isRefreshing: Boolean)
}