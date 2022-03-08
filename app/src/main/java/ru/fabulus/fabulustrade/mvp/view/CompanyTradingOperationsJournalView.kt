package ru.fabulus.fabulustrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface CompanyTradingOperationsJournalView : MvpView {
    fun init()
    fun updateRecyclerView()
    fun setCompanyName(name: String?)
}