package ru.fabulus.fabulustrade.mvp.view.trader

import android.widget.Toast
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic

@StateStrategyType(AddToEndStrategy::class)
interface TraderMainView : MvpView {
    fun init()
    fun initVP(traderStatistic: TraderStatistic, trader: Trader)
    fun setSubscribeBtnActive(isActive: Boolean)
    fun setObserveVisibility(isVisible: Boolean)
    fun setObserveActive(isActive: Boolean)
    fun setUsername(username: String)
    fun setProfit(profit: String, textColor: Int)
    fun setAvatar(avatar: String)
    fun setObserveChecked(isChecked: Boolean)
    fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT)
}