package ru.fabulus.fabulustrade.mvp.view.registration.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface RegAsTraderSecondView : MvpView {
    fun init()
    fun setBirthdayError()
    fun showBirthdayDataPicker(date: Long)
    fun setTraderBirthday(date: String)
    fun showToast(msg: String)
}