package ru.fabulus.fabulustrade.mvp.presenter.traderme

import android.graphics.Color
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.mvp.presenter.base.BaseTraderMvpPresenter
import ru.fabulus.fabulustrade.mvp.view.traderme.TraderMeMainView
import ru.fabulus.fabulustrade.util.formatDigitWithDef

class TraderMeMainPresenter : BaseTraderMvpPresenter<TraderMeMainView>() {

    companion object {
        private const val LOG = "VVV"
    }

    override fun onFirstViewAttach() {
        Log.d(LOG, "TraderMeMainPresenter. onFirstViewAttach()")
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
        Log.d(LOG, "TraderMeMainPresenter. loadTraderStatistic()")
        profile.user?.id?.let {
            apiRepo
                .getTraderStatistic(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ traderStatistic: TraderStatistic ->

                    var tmpColor = resourceProvider.getColor(R.color.colorDarkGray)
                    var tmpProfit = resourceProvider.getStringResource(R.string.empty_profit_result)

                    traderStatistic.colorIncrDecrDepo365?.let { colorItem ->
                        tmpProfit = resourceProvider.formatDigitWithDef(
                            R.string.incr_decr_depo_365,
                            colorItem.value
                        )

                        colorItem.color?.let { color ->
                            tmpColor = Color.parseColor(color)
                        }
                    }

                    viewState.setProfit(tmpProfit, tmpColor)
                    viewState.initVP(traderStatistic)
                }, { t ->
                    t.message
                })
        }
    }

}