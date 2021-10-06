package ru.wintrade.mvp.view.traders

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.traders.TradersFilterPresenter

@StateStrategyType(AddToEndSingleStrategy::class)
interface TradersFilterView : MvpView {
    fun init()
    fun setFilterCheckBoxState(checkedFilter: TradersFilterPresenter.CheckedFilter)
}