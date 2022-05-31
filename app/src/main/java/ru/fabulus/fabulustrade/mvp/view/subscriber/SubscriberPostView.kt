package ru.fabulus.fabulustrade.mvp.view.subscriber

import android.content.Intent
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface SubscriberPostView : MvpView {
    fun init()
    fun updateAdapter()
    fun withoutSubscribeAnyTrader()

    @StateStrategyType(SkipStrategy::class)
    fun showMessageSureToAddToBlacklist(traderId: String)

    @StateStrategyType(SkipStrategy::class)
    fun showMessagePostAddedToBlacklist()

    @StateStrategyType(SkipStrategy::class)
    fun share(shareIntent: Intent)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(msg: String)
    fun showComplainSnackBar()
}