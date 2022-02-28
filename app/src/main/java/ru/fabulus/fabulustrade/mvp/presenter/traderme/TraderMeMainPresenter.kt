package ru.fabulus.fabulustrade.mvp.presenter.traderme

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.mvp.presenter.base.BaseTraderMvpPresenter
import ru.fabulus.fabulustrade.mvp.view.traderme.TraderMeMainView

class TraderMeMainPresenter : BaseTraderMvpPresenter<TraderMeMainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadTraderStatistic()
        profile.user?.let {
            viewState.setSubscriberCount(it.subscriptionsCount)
            viewState.setUsername(it.username)
            it.avatar?.let { viewState.setAvatar(it) }
        }
    }

    private fun loadTraderStatistic() {
        profile.user?.id?.let {
            apiRepo
                .getTraderStatistic(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ traderStatistic: TraderStatistic ->
                    val (tmpColor, tmpProfit) = getProfitAndColor(traderStatistic)
                    viewState.setProfit(tmpProfit, tmpColor)
                    viewState.initVP(traderStatistic)
                }, { t ->
                    t.message
                })
        }
    }
}