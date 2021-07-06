package ru.wintrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface QuestionView : MvpView {
    fun init()
    fun setEmail(email: String)
    fun showToast()
    fun clearField()
}