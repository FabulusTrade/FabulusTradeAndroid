package ru.fabulus.fabulustrade.mvp.presenter

import android.util.Log
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.Trade
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.TradeDetailView
import ru.fabulus.fabulustrade.ui.fragment.TradeDetailFragment
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.formatString
import ru.fabulus.fabulustrade.util.isLocked
import ru.fabulus.fabulustrade.util.toStringFormat
import javax.inject.Inject

class TradeDetailPresenter(val trade: Trade) : MvpPresenter<TradeDetailView>() {

    companion object {
        const val NEW_ARGUMENT_RESULT = "NEW_ARGUMENT_RESULT"
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    private var imagesToAdd = mutableListOf<ByteArray>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

        trade.trader?.username?.let { username -> viewState.setName(username) }
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

        profile.user?.let { user ->
            if (trade.traderId != user.id) {
                viewState.setMode(TradeDetailFragment.Mode.NOT_TRADER_NO_ARGUMENT)
            } else {
                if (trade.posts == null) {
                    viewState.setMode(TradeDetailFragment.Mode.TRADER_NO_ARGUMENT)
                } else {
                    viewState.setMode(TradeDetailFragment.Mode.TRADER_HAS_ARGUMENT)
                    initArgument()
                }
            }
        }

        if (trade.subtype?.length > 0) {
            if (trade.subtype.subSequence(0, 8) == "Открытие") {
                viewState.setTradeType(TradeDetailFragment.TradeType.OPENING_TRADE)
            } else {
                viewState.setTradeType(TradeDetailFragment.TradeType.CLOSING_TRADE)
            }
        }
    }

    private fun initArgument() {
        apiRepo
            .getArgumentByTrade(profile.token!!, trade.id.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ arguments ->
                val argument = arguments.results[0]
                argument.takeProfit?.let{viewState.setTakeProfit(it)}
                argument.stopLoss?.let{viewState.setStopLoss(it)}
                argument.dealTerm?.let{viewState.setDealTerm(it)}

            }, { error ->
                Log.d(BasePostPresenter.TAG, "Error: ${error.message.toString()}")
                Log.d(BasePostPresenter.TAG, error.printStackTrace().toString())
            })
    }

    fun onPublishClicked(
        text: String,
        stopLoss: Float?,
        takeProfit: Float?,
        dealTerm: Int?,
    ) {
        if (text.length < 2 && stopLoss == null && takeProfit == null && dealTerm == null
            && (imagesToAdd.size < 1 || imagesToAdd.size > 4)
        )
            return
        createArgument(text, stopLoss, takeProfit, dealTerm)
    }

    private fun createArgument(
        text: String,
        stopLoss: Float?,
        takeProfit: Float?,
        dealTerm: Int?
    ) {
        apiRepo
            .createArgument(
                profile.token!!, profile.user!!.id,
                text,
                trade.id.toString(),
                imagesToAdd,
                stopLoss,
                takeProfit,
                dealTerm
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ post ->
                router.sendResult(NEW_ARGUMENT_RESULT, post)
                router.exit()
            }, {
                it.printStackTrace()
            })
    }

    fun closeClicked() {
        router.exit()
    }
}