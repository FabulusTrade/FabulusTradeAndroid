package ru.wintrade.mvp.view

import androidx.fragment.app.Fragment
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface LoadingView: MvpView {
    fun init(fragmentsList: ArrayList<Fragment>)
}