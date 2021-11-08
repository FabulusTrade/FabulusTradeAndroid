package ru.wintrade.mvp.view.registration.trader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.wintrade.mvp.presenter.registration.trader.ProfileState

@StateStrategyType(AddToEndSingleStrategy::class)
interface RegAsTraderThirdView : MvpView {
    fun init()
    fun showSuccessfulPatchData()
    fun showErrorPatchData(e: Throwable)
    fun renderInstructionText(state: ProfileState)
}