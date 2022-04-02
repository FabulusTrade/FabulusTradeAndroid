package ru.fabulus.fabulustrade.mvp.view.trader

import android.content.Intent
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter

@StateStrategyType(AddToEndStrategy::class)
interface TraderMePostView : MvpView {
    fun init()
    fun setBtnsState(state: TraderMePostPresenter.ButtonsState)
    fun updateAdapter()

    @StateStrategyType(SkipStrategy::class)
    fun share(shareIntent: Intent)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(msg: String)
    fun showComplainSnackBar()
    @StateStrategyType(SkipStrategy::class)
    fun showQuestionToFlashDialog(onClickYes: () -> Unit)
    @StateStrategyType(SkipStrategy::class)
    fun showMessagePostIsFlashed()
}