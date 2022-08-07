package ru.fabulus.fabulustrade.mvp.presenter

import android.os.Handler
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Argument
import ru.fabulus.fabulustrade.mvp.model.entity.Trade
import ru.fabulus.fabulustrade.mvp.view.TradeArgumentView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.fragment.TradeDetailFragment
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.formatString
import ru.fabulus.fabulustrade.util.toStringFormat

class TradeArgumentPresenter(val trade: Trade) : BasePostPresenter<TradeArgumentView>() {

    var argument: Argument? = null

    companion object {
        private const val TAG = "PostDetailPresenter"
    }

    private var isOpeningTrade: Boolean = true

    override fun onFirstViewAttach() {
        App.instance.appComponent.inject(this)
        super.onFirstViewAttach()

        initMenu()

        viewState.setCurrentUserAvatar(profile.user!!.avatar!!)

        viewState.setRepostCount(argument?.repostCount.toString())

        viewState.setType(trade.operationType)
        viewState.setCompany(trade.company)
        viewState.setTicker(trade.ticker)
        viewState.setPrice(
            resourceProvider.formatDigitWithDef(
                R.string.tv_trade_detail_price_text,
                trade.price
            )
        )
        viewState.setPriceTitle(
            resourceProvider.formatString(
                R.string.tv_trade_detail_price_title_text,
                trade.currency
            )
        )
        viewState.setDate(trade.date.toStringFormat("dd.MM.yyyy / HH:mm"))

        viewState.setSubtype(trade.subtype)

        if (trade.subtype?.length > 0) {
            if (trade.subtype.subSequence(0, 8) == "Открытие") {
                viewState.setTradeType(TradeDetailFragment.TradeType.OPENING_TRADE)
            } else {
                viewState.setTradeType(TradeDetailFragment.TradeType.CLOSING_TRADE)
            }
        }

        if (trade.subtype?.length > 0) {
            if (trade.subtype.subSequence(0, 8) == "Открытие") {
                isOpeningTrade = true
                viewState.setTradeType(TradeDetailFragment.TradeType.OPENING_TRADE)
            } else {
                isOpeningTrade = false
                viewState.setTradeType(TradeDetailFragment.TradeType.CLOSING_TRADE)
            }
        }

        initArgument()
    }

    private fun initArgument() {
        apiRepo
            .getArgumentByTrade(profile.token!!, trade.id.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ arguments ->
                argument = arguments.results[0]
                super.assignArgument(argument!!)
                super.initPost()
                if (isOpeningTrade) {
                    argument!!.takeProfit?.let { viewState.setTakeProfit(it) }
                    argument!!.stopLoss?.let { viewState.setStopLoss(it) }
                } else {
                    trade.profitCount?.let { profit ->
                        if (profit < 0.0) {
                            viewState.setLoss(profit, 2)
                        } else {
                            viewState.setProfit(profit, 2)
                        }
                    }
                }
                argument!!.dealTerm?.let {
                    if (isOpeningTrade) {
                        viewState.setDealTerm(it, 0)
                    } else {
                        viewState.setDealTerm(it, 2)
                    }
                }
            }, { error ->
                Log.d(BasePostPresenter.TAG, "Error: ${error.message.toString()}")
                Log.d(BasePostPresenter.TAG, error.printStackTrace().toString())
            })
    }

    private fun initMenu() {
        argument?.let {
            if (isSelfArgument(argument!!)) {
                viewState.setPostMenuSelf(argument!!)
            } else {
                fillComplaints()
            }
        }
    }

    private fun fillComplaints() {
        apiRepo
            .getComplaints(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ complaintList ->
                argument?.let { viewState.setPostMenuSomeone(it, complaintList) }
            }, { error ->
                Log.d(TAG, "Error: ${error.message.toString()}")
                Log.d(TAG, error.printStackTrace().toString())
            }
            )
    }

    private fun isSelfArgument(argument: Argument): Boolean {
        return argument.traderId == profile.user?.id
    }
}