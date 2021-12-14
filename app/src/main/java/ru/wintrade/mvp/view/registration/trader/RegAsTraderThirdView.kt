package ru.wintrade.mvp.view.registration.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface RegAsTraderThirdView : MvpView {
    fun init()
    fun showSuccessfulPatchData()
    fun showErrorPatchData(e: Throwable)
    fun renderInstructionText(text: String)
    fun showSelectedBrokerDialog(msg: String)
}