package ru.fabulus.fabulustrade.mvp.view.traderme

import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.mvp.view.base.BaseTraderView

@StateStrategyType(AddToEndStrategy::class)
interface TraderMeMainView: BaseTraderView {
    fun init()
    fun setProfit(profit: String, textColor: Int)
    fun setUsername(username: String)
    fun setSubscriberCount(count: Int)
    fun initVP(traderStatistic: TraderStatistic)
}