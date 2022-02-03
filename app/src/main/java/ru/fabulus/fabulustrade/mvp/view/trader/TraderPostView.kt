package ru.fabulus.fabulustrade.mvp.view.trader

import android.content.Intent
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface TraderPostView : MvpView {
    fun init()
    fun updateAdapter()
    fun isAuthorized(isAuth: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun share(shareIntent: Intent)

    fun incRepostCount()

    @StateStrategyType(SkipStrategy::class)
    fun showToast(msg: String)
}