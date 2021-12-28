package ru.fabulus.fabulustrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface CompanyTradingOperationsView : MvpView {
    fun init()
    fun updateRecyclerView()
    fun setCompanyName(name: String?)
}