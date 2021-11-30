package ru.wintrade.mvp.presenter

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Trade
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.view.TradeDetailView
import ru.wintrade.util.formatDigitWithDef
import ru.wintrade.util.formatString
import ru.wintrade.util.toStringFormat
import javax.inject.Inject

class TradeDetailPresenter(val trade: Trade) : MvpPresenter<TradeDetailView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var resourceProvider: ResourceProvider

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
        viewState.setCount(trade.count.toString())
        viewState.setSum(
            resourceProvider.formatDigitWithDef(
                R.string.tv_trade_detail_sum_text,
                trade.value
            )
        )
        viewState.setSumTitle(
            resourceProvider.formatString(
                R.string.tv_trade_detail_sum_title_text,
                trade.currency
            )
        )
        viewState.setSubtype(trade.subtype)
    }

    fun closeClicked() {
        router.exit()
    }
}