package ru.fabulus.fabulustrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun init()
    fun setupHeader(
        isTrader: Boolean,
        avatar: String? = null,
        username: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        email: String? = null,
        phone: String? = null
    )

    fun exit()
}