package ru.wintrade.mvp.view.subscriber

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.subscriber.SubscriberTradePresenter

@StateStrategyType(AddToEndStrategy::class)
interface SubscriberDealView : MvpView {
    fun init()
    fun updateAdapter()
    fun setBtnState(state: SubscriberTradePresenter.State)

    @StateStrategyType(SkipStrategy::class)
    fun setRefreshing(isRefreshing: Boolean)

    fun withoutSubscribeAnyTrader()
}