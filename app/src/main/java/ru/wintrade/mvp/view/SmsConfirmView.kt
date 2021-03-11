package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SmsConfirmView: MvpView {
    fun init()
    @StateStrategyType(SkipStrategy::class)
    fun showToast(msg: String)
}