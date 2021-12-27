package ru.fabulus.fabulustrade.mvp.view.traderme

import android.widget.Toast
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderMeObservationView: MvpView {
    fun init()
    fun updateAdapter()
    fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT)
}