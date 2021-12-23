package ru.wintrade.mvp.view.entrance

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SplashView: MvpView {
    fun init()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun goToMain()
}