package ru.fabulus.fabulustrade.mvp.view.generalfeed

import android.content.Intent
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter

@StateStrategyType(AddToEndStrategy::class)
interface GeneralFeedView : MvpView {
    fun init()
    fun setBtnsState(state: TraderMePostPresenter.ButtonsState)
    fun updateAdapter()
    fun isAuthorized(isAuth: Boolean)
    @StateStrategyType(SkipStrategy::class)
    fun detachAdapter()

    @StateStrategyType(SkipStrategy::class)
    fun attachAdapter()

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