package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.model.Traders

@StateStrategyType(AddToEndSingleStrategy::class)
interface AllTradersView : MvpView {
    fun init()
    fun updateRecyclerView()
}